package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.po.User;
import pojo.vo.Message;

public interface UserService {
    /**
     * 通过userId查找用户
     *
     * 根据用户的id号查找该用户的个人资料
     * @param userId
     * @return 找得到返回User, 否则返回fasle
     * @return 返回user
     */
    User selectUserById(int userId);


    /**
     * 注册用户
     *
     * @return
     */
    Message<?> createUser(HttpServletRequest request);


    User getMsgById(int userId);


}
