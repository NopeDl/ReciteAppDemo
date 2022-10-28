package pkserver;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import pojo.vo.MatchInf;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yeyeye
 * @Date 2022/10/18 16:34
 */
public class StatusPool {
    private static final Logger logger = LoggerFactory.getLogger(StatusPool.class);

    /**
     * 匹配池
     */
    public static final Map<MatchInf,PkUser> MATCHING_POOL = new ConcurrentHashMap<>();

    /**
     * 将互相匹配到的人放在一起
     */
    public static final Map<Integer,Integer> PK = new ConcurrentHashMap<>();

    /**
     * 比赛池
     */
    public static final Map<MatchInf,PkUser> MATCHED_POOL = new ConcurrentHashMap<>();

    /**
     * 房间总数
     */
    public static final List<PkRoom> PK_ROOM_LIST = Collections.synchronizedList(new ArrayList<>());

    /**
     * 进入匹配池
     * @param pkUser 要匹配的用户
     * @param matchInf 匹配用户的信息
     */
    public static void enterMatchingPool(MatchInf matchInf,PkUser pkUser){
        MATCHING_POOL.put(matchInf,pkUser);
        logger.debug("用户：" + matchInf.getUserId() + "进入匹配池");
    }

    /**
     * 退出匹配
     */
    public static void quitMatchingPool(MatchInf matchInf){
        MATCHING_POOL.remove(matchInf);
        logger.debug("用户：" + matchInf.getUserId() + "离开匹配池");
    }

    /**
     * 进入比赛池
     * @param pkUser 比赛的用户
     */
    public static void enterMatchedPool(MatchInf matchInf,PkUser pkUser){
        MATCHED_POOL.put(matchInf,pkUser);
        logger.debug("用户：" + matchInf.getUserId() + "进入比赛池");
    }

    /**
     * 离开比赛池
     */
    public static void quitMatchedPool(MatchInf matchInf){
        MATCHED_POOL.remove(matchInf);
        logger.debug("用户：" + matchInf.getUserId() + "离开比赛池");
    }


    public static void getEnemyPlayer(MatchInf curPlayerMatchInf){
        //获取对手用户ID
        int curPlayerId = MATCHED_POOL.get(curPlayerMatchInf).getMatchInf().getUserId();
        Integer enemyId = PK.get(curPlayerId);

    }

}
