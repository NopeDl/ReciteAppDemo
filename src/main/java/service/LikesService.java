package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.vo.Message;

import java.util.Set;

public interface LikesService {

//    /**
//     * 将USER_LIKE 和 MODLE_LIKE 与数据库查到的东西进行比较
//     * 然后实现对点赞表的增删改除
//     * @return 返回true or false
//     */
//    boolean compare();
//
//
//    /**
//     * 将USER_LIKE和MODLE_LIKE缓存的东西进行删除并且更新
//     * @return 返回true or false
//     */
//    boolean update();


    /**
     * 用户执行点赞或者取消点赞
     * @param request
     */
    Message likeOrDisLike(HttpServletRequest request);


    /**
     * 更改缓存数量
     * @param modleId 模板id
     * @param great 变化的数量 点赞为+1，取消点赞为-1
     */
    void updateData(int modleId,int great);

    /**
     * 点赞
     * @param userId
     * @param modleId
     */
    void like(int userId,int modleId);


    /**
     * 取消点赞
     * @param userId
     * @param modleId
     */
    void disLike(int userId,int modleId);


    /***
     * 将缓存里的数据在like表和modeId表中进行操作
     * 还要将缓存里的东西全部更新
    */
    void updateLikes();


    /**
     * 更改缓存USER_LIKE里的内容
     * @param userId 用户id
     * @param set 用户点赞的modleId集合
     */
     void updateUSER_LIKE(int userId,Set<Integer> set) ;

    /**
     * 执行取消点赞后的关系，其中USER_LIKE肯定包含userId的键
     * @param userId
     * @param set
     */
    void deleteUSER_LIKE(int userId,Set<Integer> set) ;

    /**
     * 更新缓存中点赞数量的操作
     * @param modleId
     * @param great
     */
    void updateMODLE_LIKE(int modleId,int great);

    /**
     * 根据模板id来返回模板的点赞数量
     * @param modleId
     * @return
     */
    int getLikeNumsByModleId(int modleId);

    /**
     * 初始化总缓存
     */
    void initCaChe();
}
