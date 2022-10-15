package service.impl;


import dao.DateDao;
import dao.ModleDao;
import dao.UserDao;
import dao.impl.DateDaoImpl;
import dao.impl.ModleDaoImpl;
import dao.impl.UserDaoImpl;
import enums.MsgInf;
import jakarta.servlet.http.HttpServletRequest;
import pojo.po.User;
import pojo.vo.Message;
import service.UserService;

import java.util.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();

    private final ModleDao modleDao = new ModleDaoImpl();
    private final DateDao dateDao = new DateDaoImpl();

    /**
     * 注册用户
     *
     * @param request
     * @return
     */
    @Override
    public Message createUser(HttpServletRequest request) {
        String number = request.getParameter("phone");
        String password = request.getParameter("password");
        String nickName = request.getParameter("username");
        int ret = userDao.createUserByNumber(number, password, nickName);
        Message message;
        if (ret == 1) {
            User user = userDao.selectUserByNickName(nickName);
            int userId = user.getUserId();
            message = new Message(MsgInf.OK);
            message.addData("isSuccess", true);
            message.addData("userId", userId);//将id发送给前端
        } else {
            message = new Message("用户创建失败");
            message.addData("isSuccess", true);
        }
        return message;
    }

    @Override
    //通过userId来查找用户资料
    public Message selectUserMsg(HttpServletRequest request) {

        Message message;
        int userId = Integer.parseInt(request.getParameter("userId"));
        User user = userDao.selectUserById(userId);
        //将响应的数据封装到message里
        message = new Message(MsgInf.OK);
        message.addData("user", user);
        return message;
    }

    /**
     * 通过用户id来改个人信息
     *
     * @param userId
     * @param request
     * @return
     */
    @Override
    public Message ReMsgById(int userId, HttpServletRequest request) {
        Message message;
        String nickName = request.getParameter("userName");
        String sex = request.getParameter("sex");
        //将生日转化为data
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
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

        User user = new User();
        user.setUserId(userId);
        user.setNickName(nickName);
        user.setBirthday(birthday);
        user.setImage(imagePath);
        user.setPoints(points);
        user.setCityId(cityId);
        user.setSex(sex);
        user.setSchool(schoolId);

        int result = userDao.reMessageById(user);
        if (result > 0) {
            //把修改后的资料带走
            message = new Message(MsgInf.OK);
            message.addData("user", user);
        } else {
            message = new Message("该修改违法！");
            message.addData("isSuccess", false);
        }
        return message;
    }

    /**
     * 检查昵称是否可用
     *
     * @param request
     * @return
     */
    @Override
    public Message checkNickNameExists(HttpServletRequest request) {
        String nickName = request.getParameter("username");
        String name = userDao.selectNickName(nickName);
        Message msg;
        if (name == null) {
            //用户名可用
            msg = new Message("用户名可用");
            msg.addData("isOk", true);
        } else {
            //用户名不可用
            msg = new Message("用户名已被使用");
            msg.addData("isOk", false);
        }
        return msg;
    }

    /**
     * 获取积分榜前十
     *
     * @param request
     * @return
     */
    @Override
    public Message rankingList(HttpServletRequest request) {
        List<User> userList = userDao.selectTopTen();
        Message msg;
        if (userList.size() > 0) {
            msg = new Message("获取成功");
            msg.addData("ranking", userList);
        } else {
            msg = new Message("获取失败");
        }
        return msg;
    }

    /**
     * 获取用户排位和信息
     *
     * @param request
     * @return
     */
    @Override
    public Message userRanking(HttpServletRequest request) {
        Message msg;
        int userId = Integer.parseInt(request.getParameter("userId"));
        Integer userRanking = userDao.selectUserRanking(userId);//查询用户排名
        User user = userDao.selectUserById(userId);//查询用户信息
        Map<String, Object> res = new HashMap<>();//封装数据
        res.put("userRanking", userRanking);
        res.put("user", user);
        msg = new Message("获取成功");
        msg.addData("userData", res);
        return msg;
    }

    @Override
    public Message clockIn(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(request.getParameter("date"));
        } catch (ParseException e) {
            throw new RuntimeException("日期解析有误");
        }
        int i = dateDao.insertDateByUserId(userId, date);
        Message msg;
        if (i > 0) {
            msg = new Message("打卡成功");
            msg.addData("isSuccess", true);
        } else {
            msg = new Message("打卡失败");
            msg.addData("isSuccess", false);
        }
        return msg;
    }

    /**
     * 获取打卡记录
     *
     * @param request
     * @return
     */
    @Override
    public Message getClockInRecord(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        List<String> list = dateDao.selectDateByUserId(userId);
        Message msg;
        if (list != null) {
            msg = new Message("查找成功");
            msg.addData("dateList", list);
        } else {
            msg = new Message("查找失败");
        }
        return msg;
    }
}
