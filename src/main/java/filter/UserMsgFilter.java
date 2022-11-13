package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import tools.utils.ResponseUtil;

import java.io.IOException;

/**
 * 拦截需要UserId但又没有的非法请求
 *
 * @author y and i
 */
@WebFilter({
        "/user.do/UserMsg", "/user.do/ReMessage", "/user.do/userRanking", "/user.do/clockIn", "/user.do/getClockInRecord", "/user.do/storeDSSD",
        "/inf.get/studyData", "/inf.get/getRandomModles", "/inf.get/getModlesByTag", "/inf.get/getAccuracy",
        "/modle/MakeModle", "/modle/deleteModle", "/modle/UserMemory", "/modle/Collection", "/modle/UpdateModleStatus",
        "/upload/uploadImg",
        "/review/JoinThePlane", "/review/RemoveFromPlan", "/review/GetPeriodModle", "/review/FinishOnceReview"
})
public class UserMsgFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //登录拦截
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            Message msg = new Message("需要登录才能访问");
            msg.addData("uri", "login.html");
            ResponseUtil.send(response, msg);
        } else {
            chain.doFilter(request, response);
        }
    }
}
