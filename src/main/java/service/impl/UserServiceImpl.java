package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import enums.MsgInf;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import pojo.po.User;
import pojo.vo.Message;
import service.UserService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();

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
    public Message selectUserMsg(HttpServletRequest request) {

        Message<?> message;
//        int userId = Integer.parseInt(getCookie(request, "userId"));//查找userId
        int userId = (int) request.getSession().getAttribute("userId");//通过session获取userId
        User user = userDao.selectUserById(userId);
        //将响应的数据封装到message里
        message = new Message<User>(MsgInf.OK, user);
        return message;
    }

    /**
     * 目前没啥用
     * @param request
     * @param cookieName
     * @return
     */
    @Override
    public String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        //如果cookies为空，直接返回null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public Message ReMsgById(int userId,HttpServletRequest request) {
        Message<?> message;
        String nickName = request.getParameter("userName");
        String sex = request.getParameter("sex");
            //将生日转化为data
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = null;
        try {
            birthday = fmt.parse(request.getParameter("birthday"));
            } catch (ParseException e) {
                e.printStackTrace();
        }

        Integer points = Integer.parseInt(request.getParameter("points"));
        String imagePath = request.getParameter("imagePath");
        Integer cityId = Integer.parseInt(request.getParameter("cityId"));
        String schoolId = request.getParameter("schoolId");

        User user = new User(userId, nickName, sex, birthday, points, imagePath, cityId, schoolId);

        int result = userDao.reMessageById(user);
        if(result>0){
            //把修改后的资料带走
            message=new Message<>(MsgInf.OK, user);
        }else{
            message=new Message<>("该修改违法！");
        }
        return message;
    }
}
