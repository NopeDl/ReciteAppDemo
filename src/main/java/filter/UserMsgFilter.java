package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import utils.ResponseUtil;

import java.io.IOException;

//拦截用户未登录非法请求
@WebFilter({"/user.do/UserMsg", "/user.do/ReMessage", "/user.do/ChangePswd", "/upload/*"})
public class UserMsgFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//        通过session域中是否存在userid判断是否登录
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String userId = request.getParameter("userId");//临时解决？（但session失效暂时没有想到什么好办法）
        if (userId == null) {
            //此时没有userId,说明用户没有进行登录，跳转到登录页面
//            response.sendRedirect((request.getContextPath() + "/login.html"));
            Message msg = new Message("需要登录才能访问");
            msg.addData("uri", "login.html");
            ResponseUtil.send(response, msg);
        } else {
            //否则放行
            chain.doFilter(request, response);
        }
    }
}
