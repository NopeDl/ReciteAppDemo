package wsserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import javax.net.ssl.SSLSession;
import java.net.InetSocketAddress;

public class PKServer extends WebSocketServer {
    private PKServiceDispatcher dispatcher = new PKServiceDispatcher();
    public PKServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        String uri = clientHandshake.getResourceDescriptor();

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("服务器关闭");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println(webSocket);
        System.out.println("Message:" + s);
        webSocket.send("ok");
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        System.out.println("PK服务器启动");
    }
}
