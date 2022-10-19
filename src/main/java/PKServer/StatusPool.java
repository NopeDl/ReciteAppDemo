package PKServer;

import javafx.beans.binding.MapExpression;
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
    public static   Map<MatchInf,PkUser> MATCHING_POOL = new HashMap<>();

    //将互相匹配到的人放在一起
    public static  Map<Integer,Integer> PK=new HashMap<>();

    /**
     * 比赛池
     */
    public  final Map<MatchInf,PkUser> MATCHED_POOL = new HashMap<>();

    /**
     * 进入匹配池
     * @param pkUser 要匹配的用户
     * @param matchInf 匹配用户的信息
     */
    public void enterMatchingPool(PkUser pkUser,MatchInf matchInf){
        MATCHING_POOL.put(matchInf,pkUser);
    }

    /**
     * 进入比赛池
     * @param pkUser 比赛的用户
     * @param matchInf
     */
    public void enterMATCHED_POOL(PkUser pkUser,MatchInf matchInf){
        MATCHED_POOL.put(matchInf,pkUser);
    }

}
