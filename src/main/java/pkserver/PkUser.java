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
import pkserver.threads.UserMatchThread;
import pojo.po.db.File;
import pojo.po.db.Modle;
import pojo.po.db.User;
import pojo.vo.MatchInf;
import pojo.vo.SocketMessage;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yeyeye
 */
@ServerEndpoint("/PK/{userId}/{modleId}/{difficulty}")
public class PkUser {

    private static final UserDao userdao = new UserDaoImpl();
    private static final ModleDao modleDao = new ModleDaoImpl();
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
            if (isValidMatch()){
                msg = startMatch();
            }else {
                msg = new SocketMessage(SocketMsgInf.MATCH_INVALID);
            }

            ResponseUtil.send(this.session, msg);
            } else if ("Ready".equals(operate)) {
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
            //锁上防止并发异常
            Lock lock = new ReentrantLock();
            lock.lock();
            try {
                //如果是json则执行扣血等操作
                this.pkRoom.excute(operate, this);
            } finally {
                lock.unlock();
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
        //发送连接成功信息
        ResponseUtil.send(this.session, new SocketMessage());
    }


    @OnClose
    public synchronized void onClose(Session session) throws IOException {
        //清空三个池中的数据
        if (pkRoom != null){
            //结束房间
            pkRoom.end();
        }else {
            //清除匹配状态
            StatusPool.MATCHING_POOL.remove(this.matchInf);
        }
        //发送关闭消息
        SocketMessage smsg = new SocketMessage(SocketMsgInf.SERVER_CLOSE);
        ResponseUtil.send(this.session, smsg);
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
     * @return bool
     */
    private boolean isValidMatch(){
        //遍历三个池
        boolean b = StatusPool.MATCHING_POOL.containsKey(this.matchInf);
        boolean b1 = StatusPool.MATCHED_POOL.containsKey(this.matchInf);
        return !(b || b1);
    }

    /**
     * 执行开始匹配方法
     */
    private SocketMessage startMatch() {
        //补全匹配信息（模板字数，难度，内容）
        Modle modle = modleDao.selectModleByModleId(this.matchInf.getModleId());
        //根据路径获取内容
        InputStream input = null;
        try {
            input = new FileInputStream(modle.getModlePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileHandler txtFileHandler = FileHandlerFactory.getHandler("txt", input);
        String content = txtFileHandler.parseContent();
        //将内容封装
        this.matchInf.setContent(content);
        this.matchInf.setModleNum(content.length());
        //将当前用户加入匹配池
        StatusPool.MATCHING_POOL.put(this.matchInf, this);
        //开始匹配
        Thread userMatchThread = new Thread(new UserMatchThread(this.matchInf));
        userMatchThread.start();
        try {
            userMatchThread.join();//等待匹配完毕再执行下面逻辑
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //匹配结束后用户会在PK池和比赛池中存在
        //查找对方的用户信息响应给客户端
        User user = null;
        //获取PK池
        Map<Integer, Integer> pkPool = StatusPool.PK;
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            if (pkPool.containsKey(this.matchInf.getUserId())) {
                //说明是自己先匹配到的，自己的信息作为键，对方信息为值
                //获取对方的信息
                Integer enemyUserId = pkPool.get(this.matchInf.getUserId());
                if (enemyUserId != null) {
                    //匹配成功
                    //根据用户id查找用户信息
                    user = userdao.selectUserById(enemyUserId);
                    String path = user.getImage();
                    try {
                        InputStream touxianginput = new FileInputStream(path);
                        FileHandler fileHandler = FileHandlerFactory.getHandler("img",touxianginput);
                        String base64pic = fileHandler.parseContent();
                        user.setBase64(base64pic);
                        input.close();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            SocketMessage msg;
            if (user != null) {
                //将用户加入房间
                PkRoom.joinRoom(this);
                //真的匹配成功了！
                msg = new SocketMessage(SocketMsgInf.MATCH_SUCCESS);
                msg.addData("enemyInf", user);
                //还需将内容自动挖空响应
                String context = StringUtil.autoDig(this.matchInf.getModleId(), this.matchInf.getDifficulty());
                msg.addData("context", context);
            } else {
                msg = new SocketMessage(SocketMsgInf.MATCH_FAILED);
            }
            return msg;
        } finally {
            lock.unlock();
        }

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
