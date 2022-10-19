package pojo.vo;

import enums.SocketMsgInf;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yeyeye
 * @Date 2022/10/19 23:24
 */
public class SocketMessage {
    /**
     * 封装数据
     */
    private final Map<String,Object> datas = new HashMap<>();


    public SocketMessage() {
        //默认连接成功
        this(SocketMsgInf.CONNECTION_TRUE);
    }

    /**
     * 枚举构造方法
     * @param socketMsgInf 枚举
     */
    public SocketMessage(SocketMsgInf socketMsgInf){
        datas.put(socketMsgInf.getName(),socketMsgInf.getStatus());
    }

    /**
     * 添加消息
     * @param key 键
     * @param o 值
     */
    public void addData(String key,Object o){
        datas.put(key,o);
    }

    public Map<String, Object> getDatas() {
        return datas;
    }
}
