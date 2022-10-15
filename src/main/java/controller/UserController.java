package controller;


import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.AccountService;
import service.ModleService;
import service.UserService;
import service.impl.AccountServiceImpl;
import service.impl.ModleServiceImpl;
import service.impl.UserServiceImpl;
import utils.ResponseUtil;
import utils.StringUtil;

import java.io.IOException;

/**
 * 执行用户相关操作
 */
@WebServlet("/user.do/*")
public class UserController extends HttpServlet {
    private final AccountService accountService = new AccountServiceImpl();
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取URI
        String requestURI = StringUtil.parseURI(request.getRequestURI());
        //根据URI类型执行对应方法
        Message msg = null;
        if ("Login".equals(requestURI)) {
            //登录
            msg = accountService.checkAccount(request, response);
        } else if ("Reg".equals(requestURI)) {
            //注册
            msg = userService.createUser(request);
        } else if ("UserMsg".equals(requestURI)) {
            //用户个人信息获取
            msg = userService.selectUserMsg(request);
        } else if ("ChangePswd".equals(requestURI)) {
            //修改密码
            msg = accountService.changePassword(request);
        } else if ("ReMessage".equals(requestURI)) {
            //修改个人信息
            Integer userId = accountService.getIdByNumber(request);
            msg = userService.ReMsgById(userId, request);
        }else if ("userRanking".equals(requestURI)) {
            //获取用户排名和信息
            msg = userService.userRanking(request);
        } else if ("clockIn".equals(requestURI)) {
            //打卡
            msg = userService.clockIn(request);
        } else if ("getClockInRecord".equals(requestURI)) {
            //获取打卡记录
            msg = userService.getClockInRecord(request);
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        //发送响应消息体
        ResponseUtil.send(response, msg);


    }
}
