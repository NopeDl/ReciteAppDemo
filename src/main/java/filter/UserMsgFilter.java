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
@WebFilter({"/user.do/UserMsg", "/user.do/ReMessage", "/user.do/ChangePswd", "/upload/*","/user.do/clockIn","/user.do/getClockInRecord"})
public class UserMsgFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //前期 无法拦截HTML等页面
//        通过session域中是否存在userid判断是否登录
//        Integer userId = (Integer) request.getSession().getAttribute("userId");

        String userId = request.getParameter("userId");//临时解决？（但session失效暂时没有想到什么好办法）
        if (userId == null){
            Message msg = new Message("需要登录才能访问");
            msg.addData("uri", "login.html");
            ResponseUtil.send(response, msg);
        }else {
            chain.doFilter(request,response);
        }
    }
}
