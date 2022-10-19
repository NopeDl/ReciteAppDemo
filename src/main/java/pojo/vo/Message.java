package pojo.vo;

import enums.MsgInf;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于封装响应前端的信息
 */
public class Message {
    /**
     * 响应状态码: 200 404 500等...
     */
    private final int code;

    /**
     * 响应的一些描述
     */
    private final String content;

    /**
     * 响应数据
     */
    private Map<String, Object> data = new HashMap<>();

    public Message() {
        //默认发送成功消息
        this(MsgInf.OK);
    }

    public Message(MsgInf msgInf) {
        this.code = msgInf.getCode();
        this.content = msgInf.getContent();
    }

    public Message(String content) {
        this.code = 200;
        this.content = content;
    }

    public void addData(String name, Object o) {
        data.put(name, o);
    }

    public int getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
