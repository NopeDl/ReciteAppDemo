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
     * @throws IOException
     */
    public static  void send(HttpServletResponse response, Message message) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", message);
        response.getWriter().write(jsonObject.toJSONString());
    }
}
