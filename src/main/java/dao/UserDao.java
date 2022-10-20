package dao;

import pojo.po.db.User;
import pojo.vo.Community;

import java.util.List;

public interface UserDao {


    /**
     * 根据用户id获取用户的昵称和头像
     * @param community
     * @return
     */
    User selectNameImgById(Community community);

    /**
     * 通过用户id获取该用户
     *
     * @param userId 需要查找的用户id
     * @return 存在则返回该用户pojo，不存在返回null
     */
    User selectUserById(int userId);

    /**
     * 通过用户id获取该用户
     *
     * @param nickName 需要查找的用户昵称
     * @return 存在则返回该用户pojo，不存在返回null
     */
    User selectUserByNickName(String nickName);

    /**
     * 根据手机号创建新用户
     *
     * @param number
     * @param password
     * @param nickName
     * @return
     */
    int createUserByNumber(String number, String password, String nickName);

    /**
     * 修改昵称
     * @param userId
     * @param nickName
     * @return
     */
    int updateNickNameByUserID(int userId,String nickName);

    /**
     * 修改头像
     * @param userId
     * @param image
     * @return
     */
    int updateImageByUserID(int userId,String image);

    /**
     * 修改电话
     * @param userId
     * @param number
     * @return
     */
    int updatePhoneByUserID(int userId,String number);

    /**
     * 获取用户名
     *
     * @param nickName
     * @return
     */
    String selectNickName(String nickName);

    List<User> selectTopTen();

    /**
     * 获取用户榜单排名
     * @param userId
     * @return
     */
    Integer selectUserRanking(int userId);
}
