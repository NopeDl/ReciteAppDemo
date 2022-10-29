package pkserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pkserver.listeners.StatusPoolListener;
import pojo.vo.MatchInf;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yeyeye
 * @Date 2022/10/18 16:34
 */
public class StatusPool {
    /**
     * 池子监听器
     */
    private StatusPoolListener listener;

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
    public void enterMatchingPool(MatchInf matchInf,PkUser pkUser){
        MATCHING_POOL.put(matchInf,pkUser);
        listener.matchingPoolAdded(pkUser);
        logger.debug("用户：" + matchInf.getUserId() + "进入匹配池");
    }

    /**
     * 退出匹配
     */
    public void quitMatchingPool(MatchInf matchInf){
        MATCHING_POOL.remove(matchInf);
        listener.matchingPoolRemoved();
        logger.debug("用户：" + matchInf.getUserId() + "离开匹配池");
    }

    /**
     * 进入比赛池
     * @param pkUser 比赛的用户
     */
    public void enterMatchedPool(MatchInf matchInf,PkUser pkUser){
        MATCHED_POOL.put(matchInf,pkUser);
        listener.matchedPoolAdded(pkUser);
        logger.debug("用户：" + matchInf.getUserId() + "进入比赛池");
    }

    /**
     * 离开比赛池
     */
    public void quitMatchedPool(MatchInf matchInf){
        MATCHED_POOL.remove(matchInf);
        listener.matcherPoolRemoved();
        logger.debug("用户：" + matchInf.getUserId() + "离开比赛池");
    }

    /**
     * 进入pk池
     * @param pkUser 比赛的用户
     */
    public void enterPkPool(PkUser pkUser,PkUser enemyPkUser){
        PK.put(pkUser.getMatchInf().getUserId(),enemyPkUser.getMatchInf().getUserId());
        listener.pkPoolAdded(pkUser);
    }

    /**
     * 离开pk池
     */
    public void quitPkPool(PkUser pkUser){
        PK.remove(pkUser.getMatchInf().getUserId());
        listener.pkPoolRemoved();
    }

    /**
     * 注册监听器
     * @param listener 监听器
     */
    public void listenerRegisty(StatusPoolListener listener){
        this.listener = listener;
    }
}
