package controller;


import com.zz.utils.StringParser;
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
        if ("Login".equals(requestURI)) {
            //登录
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            Message<?> loginMessage;
            if (password != null && phone != null) {
                //两个参数不为空
                loginMessage = accountService.checkAccount(request, response);
            } else {
                //有一个参数为空
                loginMessage = new Message<>("phone或者password参数不能为空");
            }
            ResponseUtil.send(response, loginMessage);
        } else if ("Reg".equals(requestURI)) {
            //注册
            Message<?> createUserMessage = userService.createUser(request);
            ResponseUtil.send(response, createUserMessage);
        } else if ("UserMsg".equals(requestURI)) {
            //用户个人信息获取
            Message<?> message = userService.selectUserMsg(request);
            ResponseUtil.send(response, message);

        }

    }
}
