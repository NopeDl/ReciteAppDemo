package PKServer;

import pojo.vo.MatchInf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yeyeye
 * @Date 2022/10/18 16:34
 */
public class StatusPool {
    /**
     * 匹配池
     */
    public static final Map<MatchInf,PkUser> MATCHING_POOL = new HashMap<>();

    /**
     * 将互相匹配到的人放在一起
     */
    public static final Map<Integer,Integer> PK=new HashMap<>();

    /**
     * 比赛池
     */
    public static final Map<MatchInf,PkUser> MATCHED_POOL = new HashMap<>();

    /**
     * 进入匹配池
     * @param pkUser 要匹配的用户
     * @param matchInf 匹配用户的信息
     */
    public void enterMatchingPool(MatchInf matchInf,PkUser pkUser){
        MATCHING_POOL.put(matchInf,pkUser);
    }

    /**
     * 进入比赛池
     * @param pkUser 比赛的用户
     */
    public void enterMatchedPool(MatchInf matchInf,PkUser pkUser){
        MATCHED_POOL.put(matchInf,pkUser);
    }

}
