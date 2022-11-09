package service.impl;

import dao.LikesDao;
import dao.ModleDao;
import dao.impl.LikesDaoImp;
import dao.impl.ModleDaoImpl;
import jakarta.servlet.http.HttpServletRequest;
import pojo.po.db.Likes;
import pojo.vo.Message;
import service.LikesService;
import tools.utils.Cache;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LikesServiceImpl implements LikesService {


    private static final LikesDao likesDao =new LikesDaoImp();
    private static final ModleDao modleDao=new ModleDaoImpl();
    private static final Cache cache=new Cache();


    /**
     * 执行点赞操作
     * @param request
     */
    @Override
    public Message likeOrDisLike(HttpServletRequest request) {
        Message message=null;
        int userId = Integer.parseInt(request.getParameter("userId"));
        int  modleId = Integer.parseInt(request.getParameter("modleId"));
        Boolean likeStatus = Boolean.parseBoolean(request.getParameter("likeStatus"));
        if(likeStatus){
            like(userId,modleId);
            message=new Message("点赞成功");
        }else{
            disLike(userId,modleId);
            message=new Message("取消点赞");
        }
        return message;
    }


    /**
     * 点赞
     * @param userId
     * @param modleId
     */
    @Override
    public void like(int userId, int modleId) {
        //点赞前先查询用户是否有在缓存中执行取消点赞
        boolean flag=true;
        if(Cache.CACHE_USER_DISLIKE.containsKey(userId)){
            if(Cache.CACHE_USER_DISLIKE.get(userId).contains(modleId)){
                //说明有，直接执行remove操作就行

                Set<Integer> set = Cache.CACHE_USER_DISLIKE.get(userId);
                set.remove(modleId);
                Cache.CACHE_USER_DISLIKE.put(userId,set);
                flag=false;
            }
        }

        //说明缓存的点赞里面没有这个数据。直接进行添加操作
        if(flag){
            if(Cache.CACHE_USER_LIKE.containsKey(userId)){
                Set<Integer> set = Cache.CACHE_USER_LIKE.get(userId);
                set.add(modleId);
                Cache.CACHE_USER_LIKE.put(userId,set);
            }else{
                Set<Integer> set=new HashSet<>();
                set.add(modleId);
                Cache.CACHE_USER_LIKE.put(userId,set);

            }
        }

//        System.out.println("点赞成功");
//        System.out.println("当前缓存："+Cache.CACHE_USER_LIKE);
        updateData(modleId,1);
    }


    /**
     * 取消点赞
     * @param userId
     * @param modleId
     */
    @Override
    public void disLike(int userId, int modleId) {
        boolean flag=true;
        //取消点赞前先查询缓存总是否有这条记录
        if(Cache.CACHE_USER_LIKE.containsKey(userId)){
            if(Cache.CACHE_USER_LIKE.get(userId).contains(modleId)){
                Set<Integer> set = Cache.CACHE_USER_LIKE.get(userId);
                set.remove(modleId);
                Cache.CACHE_USER_LIKE.put(userId,set);
                flag=false;
            }
        }


        if(flag){
            if(Cache.CACHE_USER_DISLIKE.containsKey(userId)){
                Set<Integer> set = Cache.CACHE_USER_DISLIKE.get(userId);
                set.add(modleId);
                Cache.CACHE_USER_DISLIKE.put(userId,set);
            }else{
                Set<Integer> set=new HashSet<>();
                set.add(modleId);
                Cache.CACHE_USER_DISLIKE.put(userId,set);
            }
            System.out.println(Cache.CACHE_USER_DISLIKE);
        }

//        System.out.println("取消点赞成功");
//        System.out.println("当前缓存："+Cache.CACHE_USER_LIKE);
//        System.out.println("当前点赞缓存"+Cache.CACHE_MODLE_LIKE);
        updateData(modleId,-1);
    }

    /**
     * 更改缓存数量
     * @param modleId 模板id
     * @param great 变化的数量 点赞为+1，取消点赞为-1
     */
    @Override
    public void updateData(int modleId, int great) {
//        System.out.println("执行updateDate来更新");
        if(Cache.CACHE_MODLE_LIKE.containsKey(modleId)){
//            存在则直接加
            Integer integer = Cache.CACHE_MODLE_LIKE.get(modleId);
//            System.out.println("缓存里的"+integer);
//            System.out.println("likeOrDisLike"+great);
//            System.out.println("_________________________________________________");
//            System.out.println(integer+great);
//            System.out.println("_________________________________________________");

            Cache.CACHE_MODLE_LIKE.put(modleId,integer+great);
        }else{
            Cache.CACHE_MODLE_LIKE.put(modleId,great);
//            System.out.println("我走这个");
        }
//        System.out.println("缓存的嗲赞数处理成功");
    }


    /**
     * 根据缓存修改like表
     */
    @Override
    public void updateLikes() {
        //执行点赞的
        Set<Integer> setUserId = Cache.CACHE_USER_LIKE.keySet();
       //上面的set存放在缓存过程中有执行点赞操作的userId集合
        for (int userId:setUserId) {
            //循环获取userId点赞的modleId

            //更新缓存里的userId和modleId的关系
            Set<Integer> setModleId = Cache.CACHE_USER_LIKE.get(userId);
            updateUSER_LIKE(userId,setModleId);

            //再更新数据库的东西

            for (int modleId:setModleId) {
                likesDao.insetIntoLikes(userId, modleId);
            }
            Cache.CACHE_USER_LIKE.remove(userId);
        }

        //执行取消点赞的
        Set<Integer> setUserIdOfUlike = Cache.CACHE_USER_DISLIKE.keySet();
        for(int userId:setUserIdOfUlike){

//            System.out.println("这里这里"+Cache.CACHE_USER_DISLIKE);
            Set<Integer> modleIdOfUlike = Cache.CACHE_USER_DISLIKE.get(userId);
            //将总缓存里点赞的取消

            System.out.println("要被取消点赞的模板id"+modleIdOfUlike);
            deleteUSER_LIKE(userId,modleIdOfUlike);
            for(int modleId:modleIdOfUlike){
                int i = likesDao.deleteLikes(userId, modleId);
//                if(i>0){
//                    System.out.println("删除成功");
//                }else{
//                    System.out.println("删除失败");
//                }
            }
            Cache.CACHE_USER_DISLIKE.remove(userId);

        }


        //执行数量的更改
        Set<Integer> set = Cache.CACHE_MODLE_LIKE.keySet();
        for (int modleId:set){
             modleDao.updateLikeNum(modleId, Cache.CACHE_MODLE_LIKE.get(modleId));

            System.out.println(Cache.CACHE_MODLE_LIKE.get(modleId));
             updateMODLE_LIKE(modleId,Cache.CACHE_MODLE_LIKE.get(modleId));
             Cache.CACHE_MODLE_LIKE.remove(modleId);
        }
//        System.out.println(Cache.CACHE_MODLE_LIKE);
//        System.out.println("缓存：模板点赞数量"+Cache.CACHE_MODLE_LIKE);
//        System.out.println("历史缓存：模板点赞数量"+Cache.MODLE_LIKE);
    }


    /**
     * 更新历史缓存中的userId和modleId的关系
     * @param userId
     */
    @Override
    public void updateUSER_LIKE(int userId,Set<Integer> set) {
        if(Cache.USER_LIKE.containsKey(userId)){
            //如果存在
            Set<Integer> set1 = Cache.USER_LIKE.get(userId);
//            System.out.println("原来的"+set1);
//            System.out.println("新增的"+set);
            set1.addAll(set);
            Cache.USER_LIKE.put(userId,set1);
        }else{
            Cache.USER_LIKE.put(userId,set);
        }
    }

    /**
     * 将缓存里取消点赞的行为执行到总缓存里
     * @param userId
     * @param set
     */
    @Override
    public void deleteUSER_LIKE(int userId, Set<Integer> set) {
        Set<Integer> set1 = Cache.USER_LIKE.get(userId);
        set1.removeAll(set);
//        Cache.USER_DISLIKE.put(userId,set1);
        Cache.USER_LIKE.put(userId,set1);
    }


    /**
     * 更新总缓存模板点赞数
     * @param modleId
     * @param great
     */
    @Override
    public void updateMODLE_LIKE(int modleId, int great) {
        if(Cache.MODLE_LIKE.containsKey(modleId)){
            Integer integer = Cache.MODLE_LIKE.get(modleId);
//            System.out.println("!!!!!!!!!!!!!!!!!!!!");
//            System.out.println(integer);
//            System.out.println(great);
//            System.out.println("总数"+(integer+great));
            Cache.MODLE_LIKE.put(modleId,integer+great);
        }else{
            //否则直接加
            Cache.MODLE_LIKE.put(modleId,great);
        }
    }

    /**
     * 根据模板id 获取点赞情况
     * @param modleId
     * @return
     */
    @Override
    public int getLikeNumsByModleId(int modleId) {
        //先查总缓存
        int totalLike=0;
        if(Cache.MODLE_LIKE.containsKey(modleId)){
            totalLike+=Cache.MODLE_LIKE.get(modleId);
            if(Cache.CACHE_MODLE_LIKE.containsKey(modleId)){
                totalLike+=Cache.CACHE_MODLE_LIKE.get(modleId);
            }
        }
        return totalLike;
    }

    /**
     * 初始化总缓存
     */
    @Override
    public void initCaChe() {
        List<Likes> likes = likesDao.selectLikes();

        for(Likes temp:likes) {
            int userId = temp.getUserId();
            int modleId = temp.getModleId();
            if (!Cache.CACHE_USER_LIKE.containsKey(userId)){
                HashSet<Integer> modleSet = new HashSet<>();
                modleSet.add(modleId);
                Cache.CACHE_USER_LIKE.put(userId,modleSet);
            }else {
                Set<Integer> modleSet = Cache.CACHE_USER_LIKE.get(userId);
                modleSet.add(modleId);
                Cache.USER_LIKE.put(userId,modleSet);
            }


            if(!Cache.MODLE_LIKE.containsKey(modleId)){
                int i=1;
                Cache.MODLE_LIKE.put(modleId,i);
            }else{
                Integer integer = Cache.MODLE_LIKE.get(modleId);
                Cache.MODLE_LIKE.put(modleId,integer+1);
            }
        }


    }
}
