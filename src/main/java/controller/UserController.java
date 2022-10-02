package controller;


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

import java.io.IOException;

/**
 * 执行用户相关操作
 */
@WebServlet("/user.do/*")
public class UserController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取URI
        String requestURI = request.getRequestURI();
        //根据URI类型执行对应方法
        if (requestURI.contains("Login")) {
            //
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            Message<?> message;
            if (password != null && phone != null) {
                //两个参数不为空
                message = accountService.checkAccount(phone, password);
            } else {
                //有一个参数为空
                message = new Message<>("phone或者password参数不能为空");
            }
            ResponseUtil.send(response, message);
        } else if (requestURI.contains("Reg")) {

        }

    }
}
