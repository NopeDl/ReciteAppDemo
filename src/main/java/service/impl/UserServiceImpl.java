package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import pojo.po.User;
import pojo.vo.Message;
import service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();


    @Override
    //通过userId来查找用户资料
    public User getMsgById(int userId) {
        User user = userDao.selectUserById(userId);
        return user;

    }
}
