package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//拦截用户获取信息
@WebFilter("/UserMsg")
public class UserMsgFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request= (HttpServletRequest) req;
        Cookie[] cookies = request.getCookies();
        String userId = null;
        for (Cookie c:cookies) {
            //查看是否能找到userId
            if("userId".equals(c.getName())){
                userId=c.getValue();
                break;
            }
        }
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
