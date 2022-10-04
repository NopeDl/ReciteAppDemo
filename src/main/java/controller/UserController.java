package controller;


import com.zz.utils.StringParser;
import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.AccountService;
import service.UserService;
import service.impl.AccountServiceImpl;
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
        Message<?> msg = null;
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
        } else {
            msg = new Message<>(MsgInf.NOT_FOUND);
        }
        //发送响应消息体
        ResponseUtil.send(response, msg);
    }
}
