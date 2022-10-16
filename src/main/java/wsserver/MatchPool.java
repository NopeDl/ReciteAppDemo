package wsserver;

import org.java_websocket.WebSocket;
import pojo.vo.MatchStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 匹配池
 *
 * 用于保存比赛状态
 */
public class MatchPool {
    /**
     * 用于储存正在匹配中的用户信息
     *
     * 每一个WebSocket代表一个匹配中的用户
     * MatchStatus为匹配用户的相关信息
     */
    private final Map<WebSocket, MatchStatus> matchingInf = new HashMap<>();


    /**
     * 将用户加入匹配队列
     * @param conn 用户
     * @param matchStatus 用户的匹配信息
     */
    public void joinMatching(WebSocket conn,MatchStatus matchStatus){
        matchingInf.put(conn,matchStatus);
    }


    public boolean isMatching(WebSocket conn){
        return matchingInf.containsKey(conn);
    }
}
