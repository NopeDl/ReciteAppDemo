package pkserver.threads;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import pkserver.PkUser;
import pkserver.StatusPool;
import pojo.vo.MatchInf;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yeyeye and isDucka
 */
public class UserMatchThread implements Runnable {
    private final PkUser curPlayer;

    private static final Lock LOCK = new ReentrantLock();

    public UserMatchThread(PkUser curPlayer) {
        this.curPlayer = curPlayer;
    }

    @Override
    public void run() {
        //在此进行循环遍历map，用来匹配对手
        List<PkUser> matchingPool = StatusPool.MATCHING_POOL;
        //获取当前用户信息
        MatchInf curPlayerInf = curPlayer.getMatchInf();
        while (matchingPool.contains(curPlayer)) {
            synchronized (this) {
                if (!matchingPool.contains(curPlayer)) {
                    return;
                }
                for (int i = 0; i < matchingPool.size(); i++) {
                    PkUser enemy = matchingPool.get(i);
                    MatchInf enemyInf = enemy.getMatchInf();
                    //计算两者的关系
                    double result = (double) enemyInf.getModleNum() / curPlayerInf.getModleNum();
                    //当字数在范围内且选择难度一样的时候，是俩个人成功匹配的前提
                    if ((result > 0.5 && result < 2) && (enemyInf.getDifficulty().equals(curPlayerInf.getDifficulty()))) {
                        //说明两个人的的能够匹配，此时还得确认是匹配到的会不会是自己
                        System.out.println(curPlayerInf.getUserId() + " 开始本轮匹配 线程名：" + Thread.currentThread().getName());
                        if (curPlayerInf.getUserId() != enemyInf.getUserId() && matchingPool.contains(curPlayer)) {
                            //匹配的不是自己
                            //先将他放在比赛池中
                            enemy.getStatusPool().enterMatchedPool(enemy, curPlayer);
                            curPlayer.getStatusPool().enterMatchedPool(curPlayer, enemy);
                            //匹配到的不是自己，应该移除自己和匹配到的对象，进入比赛的池子
                            enemy.getStatusPool().quitMatchingPool(enemy);
                            curPlayer.getStatusPool().quitMatchingPool(curPlayer);
                            break;
                        }
                        System.out.println(curPlayerInf.getUserId() + " 结束本轮匹配 线程名：" + Thread.currentThread().getName());
                    }
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}
