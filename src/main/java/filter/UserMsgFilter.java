package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;
import service.impl.UserServiceImpl;

import java.io.IOException;

//拦截用户获取信息
@WebFilter("/user.do/UserMsg")
public class UserMsgFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //调用查找cookie是否有userId
        UserService userService = new UserServiceImpl();
        String userId = userService.getCookie(request, "userId");

        if (userId == null) {
            //此时没有userId,说明用户没有进行登录，跳转到登录页面
            response.sendRedirect((request.getContextPath() + "/login.html"));
        } else {
            //否则放行
            chain.doFilter(request, response);
        }
    }
}
