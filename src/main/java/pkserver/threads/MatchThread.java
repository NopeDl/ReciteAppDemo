package pkserver.threads;

import pkserver.PkUser;
import pkserver.StatusPool;
import pojo.vo.MatchInf;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author yeyeye
 * @Date 2022/11/3 12:02
 */
public class MatchThread implements Runnable {
    /**
     * 匹配 2.0
     *
     * 匹配队列
     *      所有匹配的用户都会在此队列中
     */
    private final Queue<PkUser> matchingQueue = new LinkedList<>();

    @Override
    public void run() {
        //用于储存每一轮匹配完后的人
        Queue<PkUser> tmpQueue = new LinkedList<>();
        //获取取消匹配列表
        List<PkUser> cancelMatchingList = StatusPool.CANCEL_MATCHING_LIST;
        //执行十轮
        for (int i = 0; i < 10; i++) {
            //更新队列
            updateQueue();
            //将上一轮未被匹配的人加入队列中
            reJoinMatching(tmpQueue);
            //在队列中匹配
            //获取匹配对象
            PkUser player = matchingQueue.poll();
            if (player != null){
                //检测当前匹配用户是否离开匹配
                if (cancelMatchingList.contains(player)){
                    //如果当前用户离开匹配，则跳过本轮匹配
                    cancelMatchingList.remove(player);
                    continue;
                }
                MatchInf playerInf = player.getMatchInf();

                while (!matchingQueue.isEmpty()) {
                    //获取被对象
                    PkUser enemy = matchingQueue.poll();
                    //检测该对象是否取消匹配
                    if (cancelMatchingList.contains(enemy)) {
                        //如果取消匹配则将他从匹配队列和取消匹配列表中清除
                        cancelMatchingList.remove(enemy);
                        continue;
                    }
                    //对象没有取消匹配
                    //计算匹配条件
                    MatchInf enemyInf = enemy.getMatchInf();
                    //计算两者字数差距
                    double result = (double) enemyInf.getModleNum() / playerInf.getModleNum();
                    if (result > 0.5 && result < 2 && enemyInf.getDifficulty().equals(playerInf.getDifficulty())) {
                        //如果字数差距符合预期并且两者难度是一样的，就让他们匹配在一起
                        StatusPool enemyPool = enemy.getStatusPool();
                        StatusPool playerPool = player.getStatusPool();
                        //将两者加入匹配完成池
                        //并让对应用户的监听器开始工作
                        enemyPool.enterMatchedPool(enemy, player);
                        playerPool.enterMatchedPool(player, enemy);
                        //让两者退出匹配池
                        enemyPool.quitMatchingPool(enemy);
                        playerPool.quitMatchingPool(player);
                    }else {
                        //将没被匹配上的人加入临时队列储存
                        tmpQueue.add(enemy);
                    }
                }
            }
        }
        StatusPool.matchThread = null;
    }

    /**
     * 更新匹配队列
     * 检测匹配池中是否有新用户，如果有，则将其添加入匹配队列
     */
    private void updateQueue() {
        //将所有匹配池中用户加入匹配队列
        List<PkUser> matchingPool = StatusPool.MATCHING_POOL;
        matchingQueue.addAll(matchingPool);
    }

    /**
     * 重新加入匹配队列
     * @param tmpQueue 临时队列
     */
    private void reJoinMatching(Queue<PkUser> tmpQueue){
        while (!tmpQueue.isEmpty()){
            this.matchingQueue.add(tmpQueue.poll());
        }
    }
}
