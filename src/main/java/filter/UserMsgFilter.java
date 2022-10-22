package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import tools.utils.ResponseUtil;

import java.io.IOException;

//拦截用户未登录非法请求
@WebFilter({"*.html","/user.do/UserMsg", "/user.do/ReMessage", "/user.do/ChangePswd", "/upload/*","/user.do/clockIn","/user.do/getClockInRecord"})
public class UserMsgFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //记录未解决：跨域请求头设置好了，cookie也会失效
        //前期 无法拦截HTML等页面

        String uri = request.getRequestURI();
        if ((uri.contains("login.html") || uri.contains("homePage.html"))){
            chain.doFilter(request,response);
        }else {
            //从session和前端的参数获取
            String userId = request.getParameter("userId");//临时解决？（但session失效暂时没有想到什么好办法）
            userId = (userId==null? (String) request.getSession().getAttribute("userId"):userId);
            if (userId == null) {
                //此时没有userId,说明用户没有进行登录，跳转到登录页面或者 响应给前端（临时解决）
                if (!(uri.contains("login.html") || uri.contains("homePage.html"))){
                    //如果请求的是静态资源
                    //并且不是登录注册页和介绍页
                    response.sendRedirect(request.getContextPath() + "/login.html");
                }else {
                    //是请求接口
                    Message msg = new Message("需要登录才能访问");
                    msg.addData("uri", "login.html");
                    ResponseUtil.send(response, msg);
                }
            } else {
                //否则放行
                chain.doFilter(request, response);
            }
        }
    }
}
