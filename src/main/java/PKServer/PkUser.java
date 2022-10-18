package PKServer;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

/**
 * @author yeyeye
 */
@ServerEndpoint("/Hello")
public class PkUser {

    private static final StatusPool STATUS_POOL = new StatusPool();


    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        //进入匹配池

    }


    @OnClose
    public void onClose(Session session) throws IOException{
        session.getBasicRemote().sendText("close:" + this);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
