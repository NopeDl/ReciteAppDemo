package pkserver;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pkserver.listeners.StatusPoolListener;
import pkserver.threads.MatchThread;
import pojo.vo.MatchInf;

import java.sql.Connection;
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

    /**
     * 匹配池
     */
    public static final List<PkUser> MATCHING_POOL = Collections.synchronizedList(new ArrayList<>());

    /**
     * 比赛池
     */
    public static final Map<PkUser,PkUser> MATCHED_POOL = new ConcurrentHashMap<>();

    /**
     * 房间总数
     */
    public static final List<PkRoom> PK_ROOM_LIST = Collections.synchronizedList(new ArrayList<>());

    /**
     * 如果有玩家取消匹配则会进入以下列表
     */
    public static final List<PkUser> CANCEL_MATCHING_LIST = Collections.synchronizedList(new ArrayList<>());

    /**
     * 匹配线程
     */
    public static Thread matchThread = null;

    /**
     * 进入匹配池
     * @param pkUser 要匹配的用户
     */
    public void enterMatchingPool(PkUser pkUser){
        MATCHING_POOL.add(pkUser);
        listener.matchingPoolAdded(pkUser);
        System.out.println("用户：" + pkUser.getMatchInf().getUserId() + "进入匹配池");
        //每次有新用户进入池子都启动匹配线程
            //线程会执行十轮匹配然后关闭
        if (matchThread == null){
            matchThread = new Thread(new MatchThread());
            matchThread.start();
        }
    }

    /**
     * 退出匹配
     */
    public void quitMatchingPool(PkUser pkUser){
        MATCHING_POOL.remove(pkUser);
        listener.matchingPoolRemoved();
        System.out.println("用户：" + pkUser.getMatchInf().getUserId() + "离开匹配池");
    }

    /**
     * 进入比赛池
     * @param curPlayer 比赛的用户
     * @param enemy 比赛的用户的对手
     */
    public void enterMatchedPool(PkUser curPlayer,PkUser enemy){
        MATCHED_POOL.put(curPlayer,enemy);
        listener.matchedPoolAdded(curPlayer);
        System.out.println("用户：" + curPlayer.getMatchInf().getUserId() + "进入比赛池");
    }

    /**
     * 离开比赛池
     */
    public void quitMatchedPool(PkUser user){
        MATCHED_POOL.remove(user);
        listener.matcherPoolRemoved();
        System.out.println("用户：" + user.getMatchInf().getUserId() + "离开比赛池");
    }

    /**
     * 进入取消匹配列表
     * @param curPlayer 比赛的用户
     */
    public void enterCancelMatchingList(PkUser curPlayer){
        CANCEL_MATCHING_LIST.add(curPlayer);
        listener.cancelMatchingPoolAdded();
        System.out.println("用户：" + curPlayer.getMatchInf().getUserId() + "进入取消匹配列表");
    }

    /**
     * 离开取消匹配列表
     */
    public void quitCancelMatchingList(PkUser curPlayer){
        CANCEL_MATCHING_LIST.remove(curPlayer);
        listener.cancelMatchingPoolRemoved();
        System.out.println("用户：" + curPlayer.getMatchInf().getUserId() + "离开取消匹配列表");
    }

    /**
     * 注册监听器
     * @param listener 监听器
     */
    public void listenerRegisty(StatusPoolListener listener){
        System.out.println(listener + " 监听器已被注册 监听对象：" + this);
        this.listener = listener;
    }
}
