package dao;

import pojo.po.User;
import pojo.vo.Message;

public interface UserDao {
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
     * 用户通过自己的userId来修改个人资料
     * @param user
     * @return
     */
    int reMessageById(User user);
}
