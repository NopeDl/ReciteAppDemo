package PKServer;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import pojo.vo.MatchStatus;

import java.io.IOException;

/**
 * @author yeyeye
 */
@ServerEndpoint("/Hello/{userId}/{modleId}")
public class PkUser {

    private static final StatusPool STATUS_POOL = new StatusPool();

    private Session session;

    private MatchStatus matchStatus;


    @OnMessage
    public void onMessage(String message) throws IOException {
        if ("START".equals(message)){
            //开始匹配
            startMatch();
        }else {

        }
    }



    @OnOpen
    public void onOpen(Session session, @PathParam("userId")String userId,@PathParam("modleId")String modleId)
            throws IOException {
        this.session = session;
        this.matchStatus = new MatchStatus();
        matchStatus.setUserId(Integer.parseInt(userId));
        matchStatus.setModleId(Integer.parseInt(modleId));
        session.getBasicRemote().sendText("连接成功：userid=" + userId);
    }


    @OnClose
    public void onClose() throws IOException{
        session.getBasicRemote().sendText("close:userId=" + this.matchStatus.getUserId());
    }

    @OnError
    public void onError(Throwable throwable) {
        try {
            session.getBasicRemote().sendText("连接失败:user=" + this.matchStatus.getUserId());
            session.getBasicRemote().sendText(throwable.getLocalizedMessage());
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

    private void startMatch() {
    }
}
