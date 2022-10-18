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
    private final Map<PkUser,MatchInf> MATCHING_POOL = new HashMap<>();

    /**
     * 比赛池
     */
    private final Map<PkUser,MatchInf> MATCHED_POOL = new HashMap<>();

    /**
     * 进入匹配池
     * @param pkUser 要匹配的用户
     * @param matchInf 匹配用户的信息
     */
    public void enterMatchingPool(PkUser pkUser,MatchInf matchInf){
        MATCHING_POOL.put(pkUser,matchInf);
    }


}
