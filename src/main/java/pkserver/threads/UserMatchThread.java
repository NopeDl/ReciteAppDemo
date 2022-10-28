package pkserver.threads;

import pkserver.PkUser;
import pkserver.StatusPool;
import pojo.vo.MatchInf;

import java.util.Map;

/**
 * @author yeyeye and isDucka
 */
public class UserMatchThread implements Runnable {
    private MatchInf matchInf = null;

    public UserMatchThread(MatchInf matchInf) {
        this.matchInf = matchInf;
    }

    @Override
    public void run() {
        //在此进行循环遍历map，用来匹配对手
        // 打印值集合
        boolean flag = true;

        synchronized (this) {
            while (flag) {

                Map<MatchInf, PkUser> matchingPool = StatusPool.MATCHING_POOL;
                Map<MatchInf, PkUser> matchedPool = StatusPool.MATCHED_POOL;
                //判断以下用户是否还在池子里
                if (matchingPool.containsKey(matchInf)) {
                    for (MatchInf key : matchingPool.keySet()) {
                        //计算两者的关系
                        double result = (double) key.getModleNum() / matchInf.getModleNum();
                        //当字数在范围内且选择难度一样的时候，是俩个人成功匹配的前提
                        if ((result > 0.5 || (result > 0 && result < 2))
                                && (key.getDifficulty().equals(matchInf.getDifficulty()))) {
                            //说明两个人的的能够匹配，此时还得确认是匹配到的会不会是自己
                            if (matchInf.getUserId() != key.getUserId()) {
                                //匹配的不是自己
                                //先将他放在比赛池中
                                matchedPool.put(key, matchingPool.get(key));
                                matchedPool.put(matchInf, matchingPool.get(matchInf));
                                //将匹配的两个人凡在map里面
                                StatusPool.PK.put(matchInf.getUserId(), key.getUserId());
                                StatusPool.PK.put(key.getUserId(), matchInf.getUserId());
                                //匹配到的不是自己，应该移除自己和匹配到的对象，进入比赛的池子
                                matchingPool.remove(key);
                                matchingPool.remove(matchInf);
                                flag = false;
                                break;
                            }
                        }
                    }
                } else {
                    //被别人匹配到了
                    //或者已经断开连接
                    //结束匹配
                    flag = false;
                }
            }
        }
    }
}
