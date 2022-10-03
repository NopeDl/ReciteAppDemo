package controller;


import com.mysql.cj.Session;
import com.mysql.cj.protocol.x.Notice;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.omg.CORBA.INTERNAL;
import org.omg.PortableInterceptor.INACTIVE;
import pojo.po.User;
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
    private final AccountService accountService = new AccountServiceImpl();
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取URI
        String requestURI = request.getRequestURI();
        //根据URI类型执行对应方法
        if (requestURI.contains("Login")) {
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
        } else if (requestURI.contains("Reg")) {
            //注册
            Message<?> createUserMessage = userService.createUser(request);
            ResponseUtil.send(response, createUserMessage);
        } else if (requestURI.contains("UserMsg")) {
            //用户个人信息获取
            Message<?> message;
            int userId;
            //查找userId
            Cookie[] cookies = request.getCookies();
            for (Cookie c : cookies) {
                if ("userId".equals(c.getName())) {
                    userId = Integer.parseInt(c.getValue());
                    User user = userService.getMsgById(userId);
                    message = new Message<>();
                    //将查找的对象放进request中
                    request.setAttribute("userMsg", user);
                    ResponseUtil.send(response, message);
                    break;
                }
            }
        }

    }
}
