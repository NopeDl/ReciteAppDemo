package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import enums.MsgInf;
import jakarta.servlet.http.HttpServletRequest;
import pojo.po.User;
import pojo.vo.Message;
import service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();


    @Override
    public User selectUserById(int userId) {
        User user = userDao.selectUserById(userId);
        return user;

    }

    /**
     * 注册用户
     *
     * @param request
     * @return
     */
    @Override
    public Message<?> createUser(HttpServletRequest request) {
        String number = request.getParameter("phone");
        String password = request.getParameter("password");
        String nickName = request.getParameter("username");
        int ret = userDao.createUserByNumber(number, password, nickName);
        Message<?> message;
        if (ret == 1) {
            message = new Message<>(MsgInf.OK);
        } else {
            message = new Message<>("用户创建失败");
        }
        return message;
    }

    @Override
    //通过userId来查找用户资料
    public User getMsgById(int userId) {
        User user = userDao.selectUserById(userId);
        return user;

    }
}
