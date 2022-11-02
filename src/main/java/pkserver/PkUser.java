package pkserver;

import com.alibaba.fastjson.JSONObject;
import dao.ModleDao;
import dao.UserDao;
import dao.impl.ModleDaoImpl;
import dao.impl.UserDaoImpl;
import enums.Difficulty;
import enums.SocketMsgInf;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import pkserver.listeners.BasicStatusPoolListener;
import pkserver.threads.UserMatchThread;
import pojo.po.db.Modle;
import pojo.po.db.User;
import pojo.vo.MatchInf;
import pojo.vo.SocketMessage;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author yeyeye
 */
@ServerEndpoint("/PK/{userId}/{modleId}/{difficulty}")
public class PkUser {
    private final StatusPool statusPool = new StatusPool();


    private static final UserDao USERDAO = new UserDaoImpl();
    private static final ModleDao MODLE_DAO = new ModleDaoImpl();
    /**
     * 会话
     */
    private Session session;

    /**
     * 当前用户匹配信息
     */
    private MatchInf matchInf;

    /**
     * 当前用户比赛房间
     */
    private PkRoom pkRoom;


    @OnMessage
    public void onMessage(Session session, String operate) throws IOException {
        SocketMessage msg;
        if ("START".equals(operate)) {
            //开始匹配
            if (isValidMatch()) {
                //检验匹配是否合法（在匹配还）
                startMatch();
            } else {
                msg = new SocketMessage(SocketMsgInf.MATCH_INVALID);
                ResponseUtil.send(this.session, msg);
            }
        } else if ("READY".equals(operate)) {
            //说明匹配成功准备开始比赛了
            if (!pkRoom.isTimerAlive()) {
                //如果计时器没打开则打开计时器
                pkRoom.startTimer();
            }
            msg = new SocketMessage();

            //响应服务器准备成功的信息
            msg.addData("isReady", true);
            ResponseUtil.send(this.session, msg);
        } else if (JSONObject.isValid(operate)) {
            //如果是json则执行扣血等操作
            this.pkRoom.excute(operate, this);
        } else if ("AGAIN".equals(operate)) {
            //循环获取挖空
            if (this.pkRoom != null){
                msg = this.pkRoom.againDig(this);
                ResponseUtil.send(this.session, msg);
            }
        } else {
            msg = new SocketMessage(SocketMsgInf.OPERATE_NOTFOUND);
            ResponseUtil.send(this.session, msg);
        }
    }

    /**
     * 开启连接
     * 给客户端响应连接成功信息
     *
     * @param session    会话
     * @param userId     连接的用户ID
     * @param modleId    连接用户比赛是所使用的模板ID
     * @param difficulty 比赛难度
     * @throws IOException 异常
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, @PathParam("modleId") String modleId, @PathParam("difficulty") String difficulty)
            throws IOException {
        this.session = session;
        //封装该比赛用户信息
        this.matchInf = new MatchInf();
        matchInf.setUserId(Integer.parseInt(userId));
        matchInf.setModleId(Integer.parseInt(modleId));
        matchInf.setDifficulty(Difficulty.getRatio(difficulty));
        //注册监听器
        statusPool.listenerRegisty(new BasicStatusPoolListener(this){
            @Override
            public synchronized void pkPoolAdded(PkUser user) {
                //监听PK池
                if (user == this.getPkUser()){
                    //如果pk池中新增用户是自己
                    //说明匹配成功了
                    SocketMessage socketMessage = matchSuccess();
                    try {
                        ResponseUtil.send(user.getSession(),socketMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //发送连接成功信息
        ResponseUtil.send(this.session, new SocketMessage());
    }


    @OnClose
    public synchronized void onClose(Session session) throws IOException {
        //清空三个池中的数据
        if (StatusPool.PK_ROOM_LIST.contains(this.pkRoom)) {
            //说明已经匹配成功了但有人中途退出
            PkUser enemyUser = this.pkRoom.getPlayer01() == this ? this.pkRoom.getPlayer02() : this.getPkRoom().getPlayer01();
            //移除池中相关信息
            this.statusPool.quitMatchedPool(this.getMatchInf());
            this.statusPool.quitPkPool(this);
            //设置房间内人数
            this.pkRoom.setPlayerNum(this.pkRoom.getPlayerNum() - 1);
            if(this.pkRoom.getPlayerNum() <= 0){
                //说明两个人都退了，房间内没有参赛人员
                StatusPool.PK_ROOM_LIST.remove(this.pkRoom);
            }else {
                //向对手发送自己已经退出的消息
                enemyUser.getSession().getBasicRemote().sendText("对方已退出游戏");
            }
        } else {
            //说明是取消匹配
            //清除匹配状态
            this.statusPool.quitMatchingPool(this.matchInf);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        //发送错误报告
        try {
            throwable.printStackTrace();
            ResponseUtil.send(this.session, new SocketMessage(SocketMsgInf.SERVER_ERROR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    /**
     * 检测当前用户匹配是否合法
     *
     * @return bool
     */
    private boolean isValidMatch() {
        //遍历三个池
        boolean b = StatusPool.MATCHING_POOL.containsKey(this.matchInf);
        boolean b1 = StatusPool.MATCHED_POOL.containsKey(this.matchInf);
        return !(b || b1);
    }

    /**
     * 执行开始匹配方法
     */
    private void startMatch() {
        //补全匹配信息（模板字数，难度，内容）
        Modle modle = MODLE_DAO.selectModleByModleId(this.matchInf.getModleId());
        //根据路径获取内容
        InputStream input;
        String content = null;
        try {
            input = new FileInputStream(modle.getModlePath());
            FileHandler txtFileHandler = FileHandlerFactory.getHandler("txt", input);
            content = txtFileHandler.parseContent();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将内容封装
        this.matchInf.setContent(content);
        this.matchInf.setModleNum(content.length());
        //将当前用户加入匹配池
        this.statusPool.enterMatchingPool(this.matchInf, this);
        //开始匹配
        Thread userMatchThread = new Thread(new UserMatchThread(this.matchInf,this.statusPool));
        userMatchThread.start();
    }

    /**
     * 匹配成功后执行
     */
    public synchronized SocketMessage matchSuccess(){
        //匹配结束后用户会在PK池和比赛池中存在
        //获取PK池
        Map<Integer, Integer> pkPool = StatusPool.PK;
        //查找对方的用户信息响应给客户端
        User user;
        if (pkPool.containsKey(this.matchInf.getUserId())) {
            //说明是自己先匹配到的，自己的信息作为键，对方信息为值
            //获取对方的信息
            Integer enemyUserId = pkPool.get(this.matchInf.getUserId());
            if (enemyUserId != null) {
                SocketMessage msg;
                //匹配成功
                //根据用户id查找用户信息
                user = USERDAO.selectUserById(enemyUserId);
                String path = user.getImage();
                try {
                    if (!"".equals(path)) {
                        InputStream touxianginput = new FileInputStream(path);
                        FileHandler fileHandler = FileHandlerFactory.getHandler("img", touxianginput);
                        String base64pic = fileHandler.parseContent();
                        user.setBase64(base64pic);
                        touxianginput.close();
                    } else {
                        user.setBase64("");
                    }
                    //将用户加入房间
                    PkRoom.joinRoom(this);
                    //真的匹配成功了！
                    msg = new SocketMessage(SocketMsgInf.MATCH_SUCCESS);
                    user.setImage(null);
                    msg.addData("enemyInf", user);
                    //还需将内容自动挖空响应
                    //获取房间挖空数，并将两边挖同样数量的空
                    int blankNum = this.getPkRoom().getBlankNum();
                    String context = StringUtil.autoDig(this.matchInf.getModleId(), blankNum);
                    //添加挖好空的内容
                    msg.addData("context", context);
                    //添加总时间限制
                    msg.addData("timeLimits",this.pkRoom.getTimeLimits());
                    return msg;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //说明匹配失败了
        return new SocketMessage(SocketMsgInf.MATCH_FAILED);
    }

    public PkRoom getPkRoom() {
        return pkRoom;
    }

    public void setPkRoom(PkRoom pkRoom) {
        this.pkRoom = pkRoom;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public MatchInf getMatchInf() {
        return matchInf;
    }

    public void setMatchInf(MatchInf matchInf) {
        this.matchInf = matchInf;
    }
}
