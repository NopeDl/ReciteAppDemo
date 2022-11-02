package pkserver.threads;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import pkserver.PkUser;
import pkserver.StatusPool;
import pojo.vo.MatchInf;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yeyeye and isDucka
 */
public class UserMatchThread implements Runnable {
    private final MatchInf matchInf;

    private final StatusPool statusPool;

    private static Set<MatchInf> matchInfs;

    private static final Lock LOCK = new ReentrantLock();

    public UserMatchThread(MatchInf matchInf, StatusPool statusPool) {
        this.matchInf = matchInf;
        this.statusPool = statusPool;
    }

    @Override
    public void run() {
        //在此进行循环遍历map，用来匹配对手
        Map<MatchInf, PkUser> matchingPool = StatusPool.MATCHING_POOL;
        while (matchingPool.containsKey(matchInf)) {
            UserMatchThread.matchInfs = matchingPool.keySet();
            for (MatchInf key : matchInfs) {
                //计算两者的关系
                double result = (double) key.getModleNum() / matchInf.getModleNum();
//                LOCK.lock();
                try {
                    //当字数在范围内且选择难度一样的时候，是俩个人成功匹配的前提
                    if ((result > 0.5 && result < 2) && (key.getDifficulty().equals(matchInf.getDifficulty()))) {
                        //说明两个人的的能够匹配，此时还得确认是匹配到的会不会是自己
//                        System.out.println(matchInf.getUserId() + " 开始本轮匹配 线程名：" + Thread.currentThread().getName());
                        if (matchInf.getUserId() != key.getUserId() && matchingPool.containsKey(matchInf)) {
                            //匹配的不是自己
                            //先将他放在比赛池中
                            statusPool.enterMatchedPool(key, matchingPool.get(key));
                            statusPool.enterMatchedPool(matchInf, matchingPool.get(matchInf));
                            //将匹配的两个人凡在map里面
                            statusPool.enterPkPool(matchingPool.get(matchInf), matchingPool.get(key));
                            statusPool.enterPkPool(matchingPool.get(key), matchingPool.get(matchInf));
                            //匹配到的不是自己，应该移除自己和匹配到的对象，进入比赛的池子
                            statusPool.quitMatchingPool(key);
                            statusPool.quitMatchingPool(matchInf);
                            matchInfs.remove(key);
                            matchInfs.remove(matchInf);
                            break;
                        }
//                        System.out.println(matchInf.getUserId() + " 结束本轮匹配 线程名：" + Thread.currentThread().getName());
                    }
                }finally {
//                    LOCK.unlock();
                }

            }
        }
    }
}
