package service;

import pojo.po.User;

public interface UserService {
    /**
     * 通过userId查找用户
     * @param userId
     * @return 找得到返回User,否则返回fasle
     */
    User selectUserById(int userId);
}
