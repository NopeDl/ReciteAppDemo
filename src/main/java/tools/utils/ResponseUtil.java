package tools.utils;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Session;
import pojo.vo.Message;
import pojo.vo.SocketMessage;

import java.io.IOException;

/**
 * @author yeyeye
 */
public class ResponseUtil {

    /**
     * 处理HTTP协议
     * 将message封装成json响应
     *
     * @param response http响应
     * @param message 消息
     * @throws IOException 异常
     */
    public static  void send(HttpServletResponse response, Message message) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", message);
        response.getWriter().write(jsonObject.toJSONString());
    }

    /**
     * 处理websocket协议
     * @param session 会话
     * @param message 封装消息体
     * @throws IOException 异常
     */
    public static void send(Session session, SocketMessage message) throws IOException{
        if (session.isOpen()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("socketMsg",message);
            session.getBasicRemote().sendText(jsonObject.toJSONString());
        }
    }
}
