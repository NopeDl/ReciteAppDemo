package tools.utils;

import service.LikesService;
import service.impl.LikesServiceImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 缓存点赞有关的
 */
public class Cache {

    /**
     * 存放上一次更新前查出来的userId对modleId 的关系
     */
    public static Map<Integer, Set<Integer>> USER_LIKE = new HashMap<>();

    /**
     * 存放上一次更新前查出来的某个模板的点赞数量
     */
    public static Map<Integer, Integer> MODLE_LIKE = new HashMap<>();


    //存放缓存的userId对modleId 的关系  map里面的map<> integer存放点赞的模板id,Boolean表示点赞


    /**
     * 存放点赞的关系表
     */
    public static Map<Integer, Set<Integer>> CACHE_USER_LIKE = new HashMap<>();

    /**
     * 存放不喜欢的关系表
     */
    public static Map<Integer, Set<Integer>> CACHE_USER_DISLIKE = new HashMap<>();

    /**
     * 存放缓存的某个模板的点赞数量
     */
    public static Map<Integer, Integer> CACHE_MODLE_LIKE = new HashMap<>();


    public static LikesService likesService = new LikesServiceImpl();


    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void tiemrTask() throws InterruptedException {
        scheduler.scheduleAtFixedRate(() -> {
//                    System.out.println("*********************************");
//                    System.out.println("缓存区userId-modleId的关系"+Cache.CACHE_USER_LIKE);
//                    System.out.println("super缓存区userId-modleId的关系"+Cache.USER_LIKE);
//                    System.out.println("缓存区内userId——modleId 不喜欢的关系"+Cache.CACHE_USER_DISLIKE);
//
//                    System.out.println("缓存区内modle的点赞量"+Cache.CACHE_MODLE_LIKE);
//                    System.out.println("super缓存区内modle的点赞量"+Cache.MODLE_LIKE);
            likesService.updateLikes();
//                    System.out.println("执行时间" + LocalDate.now());
//                    try {
////                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
        }, 0, 30, SECONDS);
    }

    /**
     * 关闭缓存
     */
    public static void closeCache() {
        if (!scheduler.isShutdown()) {
            likesService.updateLikes();
            scheduler.shutdown();
        }
    }


}
