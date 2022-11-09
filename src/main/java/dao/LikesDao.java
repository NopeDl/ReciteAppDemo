package dao;

import pojo.po.db.Likes;

import java.util.List;

public interface LikesDao {

    /**
     * 查询like 表中的数据
     * @return 返回一个like类型的list
     */
    List<Likes> selectLikes();

    /**
     * 对like表执行插入
     * @return
     */
    int insetIntoLikes(int userId,int modleId);


    /**
     * 删除取消点赞的like
     * @param userId 用户id
     * @param modleId 用户要取消点赞的模板id
     * @return
     */
    int deleteLikes(int userId,int modleId);


    /**
     * 查询用户是否对某个帖子点过赞
     * @param userId 用户id
     * @param modleId 社区发布的模板id
     * @return 点赞过返回true 否则返回false
     */
    boolean selectifUserLike(int userId,int modleId);



}
