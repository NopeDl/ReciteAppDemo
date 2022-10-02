package utils;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import java.io.IOException;

public class ResponseUtil {

    /**
     * 将message封装成json响应
     *
     * @param response
     * @param message
     * @param <T>
     * @throws IOException
     */
    public static <T> void send(HttpServletResponse response, Message<T> message) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", message);
        response.getWriter().write(jsonObject.toJSONString());
    }
}
