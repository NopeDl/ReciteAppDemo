package filter;

import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//拦截用户获取信息
@WebFilter("/user.do/UserMsg")
public class UserMsgFilter implements Filter {
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //调用查找cookie是否有userId
        UserService userService=new UserServiceImpl();
        String userId=userService.getCookie((jakarta.servlet.http.HttpServletRequest) req,"userId");

        if(userId==null){
            //此时没有userId,说明用户没有进行登录，跳转到登录页面
            HttpServletResponse response= (HttpServletResponse) resp;
            response.sendRedirect("Login");
        }else{
            //否则放行
            chain.doFilter(req, resp);
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
