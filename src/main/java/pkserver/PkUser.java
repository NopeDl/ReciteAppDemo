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
import pojo.po.db.Modle;
import pojo.po.db.User;
import pojo.vo.MatchInf;
import pojo.vo.SocketMessage;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;
import tools.utils.JcsegUtil;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public synchronized void onMessage(String operate) throws IOException {
        SocketMessage msg;
        if ("START".equals(operate)) {
            //开始匹配
            if (isValidMatch()) {
                //检验匹配是否合法（在匹配还）
                try {
                    startMatch();
                } catch (IOException e) {
                    msg = new SocketMessage(SocketMsgInf.MATCH_INVALID);
                    ResponseUtil.send(this.session, msg);
                    e.printStackTrace();
                }
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
            if (this.pkRoom != null) {
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
        statusPool.listenerRegisty(new BasicStatusPoolListener(this) {
            @Override
            public void matchedPoolAdded(PkUser user) {
                //监听匹配完成池
                if (user == this.getPkUser()) {
                    //如果池中新增用户是自己
                    //说明匹配成功了
                    SocketMessage socketMessage = matchSuccess();
                    try {
                        ResponseUtil.send(user.getSession(), socketMessage);
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
    public synchronized void onClose() throws IOException {
        //清空三个池中的数据
        if (StatusPool.PK_ROOM_LIST.contains(this.pkRoom)) {
            //说明已经匹配成功了但有人中途退出
            PkUser enemyUser = this.pkRoom.getPlayer01() == this ? this.pkRoom.getPlayer02() : this.getPkRoom().getPlayer01();
            //移除池中相关信息
            this.statusPool.quitMatchedPool(this);
            //设置房间内人数
            this.pkRoom.setPlayerNum(this.pkRoom.getPlayerNum() - 1);
            if (this.pkRoom.getPlayerNum() <= 0) {
                //说明两个人都退了，房间内没有参赛人员
                StatusPool.PK_ROOM_LIST.remove(this.pkRoom);
            } else {
                //向对手发送自己已经退出的消息
                ResponseUtil.send(enemyUser.getSession(), new SocketMessage(SocketMsgInf.ENEMY_EXIT));
                this.session.close();
            }
        } else {
            //说明是取消匹配
            //清除匹配状态
            this.statusPool.enterCancelMatchingList(this);
            this.statusPool.quitMatchingPool(this);
            this.session.close();
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        //发送错误报告
        try {
            throwable.printStackTrace();
            ResponseUtil.send(this.session, new SocketMessage(SocketMsgInf.SERVER_ERROR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 检测当前用户匹配是否合法
     *
     * @return bool
     */
    private boolean isValidMatch() {
        //遍历两个个池
        boolean b = false;
        List<PkUser> matchingPool = StatusPool.MATCHING_POOL;
        for (PkUser pkUser : matchingPool) {
            if (pkUser.getMatchInf().getUserId() == this.getMatchInf().getUserId()) {
                b = true;
                break;
            }
        }
        boolean b1 = false;
        Map<PkUser, PkUser> matchedPool = StatusPool.MATCHED_POOL;
        for (PkUser pkUser : matchedPool.keySet()) {
            if (pkUser.getMatchInf().getUserId() == this.getMatchInf().getUserId()) {
                b1 = true;
                break;
            }
        }
        return !(b || b1);
    }

    /**
     * 执行开始匹配方法
     */
    private void startMatch() throws IOException {
        //补全匹配信息（模板字数，难度，内容）
        Modle modle = MODLE_DAO.selectModleByModleId(this.matchInf.getModleId());
        //根据路径获取内容
        InputStream input;
        String content;
        input = Files.newInputStream(Paths.get(modle.getModlePath()));
        FileHandler txtFileHandler = FileHandlerFactory.getHandler("txt", input);
        if (txtFileHandler == null) {
            throw new IOException("解析txt文本失败");
        }
        content = txtFileHandler.parseContent();
        input.close();
        //将内容封装
        this.matchInf.setContent(content);
        this.matchInf.setModleNum(JcsegUtil.wordCount(content));
        //将当前用户加入匹配池
        this.statusPool.enterMatchingPool(this);
        //开始匹配
    }

    /**
     * 匹配成功后执行
     */
    public synchronized SocketMessage matchSuccess() {
        //匹配结束后用户会在比赛池中存在
        //查找对方的用户信息响应给客户端
        Map<PkUser, PkUser> matchedPool = StatusPool.MATCHED_POOL;
        User user;
        if (matchedPool.containsKey(this)) {
            //获取对方的信息
            int enemyUserId = matchedPool.get(this).getMatchInf().getUserId();
            SocketMessage msg;
            //匹配成功
            //根据用户id查找用户信息
            user = USERDAO.selectUserById(enemyUserId);
            String path = user.getImage();
            try {
                if (!"".equals(path) && path != null) {
                    InputStream touxianginput = new FileInputStream(path);
                    FileHandler fileHandler = FileHandlerFactory.getHandler("img", touxianginput);
                    if (fileHandler == null) {
                        throw new IOException("解析头像失败");
                    }
                    String base64pic = fileHandler.parseContent();
                    user.setBase64(base64pic);
                    touxianginput.close();
                } else {
                    user.setBase64("");
                }
                //将用户加入房间

                PkRoom.joinRoom(this);
                System.out.println(this + " join " + this.pkRoom + " room");
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
                msg.addData("timeLimits", this.pkRoom.getTimeLimits());
                return msg;
            } catch (IOException e) {
                e.printStackTrace();
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


    public MatchInf getMatchInf() {
        return matchInf;
    }


    public StatusPool getStatusPool() {
        return statusPool;
    }
}
