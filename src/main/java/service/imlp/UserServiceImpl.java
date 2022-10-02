package service.imlp;

import pojo.po.User;
import service.UserService;

public class UserServiceImpl implements UserService {

    private  UserService userService=new UserServiceImpl();
    @Override
    public User selectUserById(int userId) {
        User user = userService.selectUserById(userId);
        return user;
    }
}
