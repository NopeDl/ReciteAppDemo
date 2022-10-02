package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import pojo.po.User;
import service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao UserDao = new UserDaoImpl();

    @Override
    public User selectUserById(int userId) {
        User user = UserDao.selectUserById(userId);
        return user;
    }
}
