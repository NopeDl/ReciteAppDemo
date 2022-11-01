package controller;

import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.ReviewService;
import service.impl.ReviewServiceImpl;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.IOException;

/**
 * 执行与复习相关的
 */
@WebServlet("/review/*")
public class ReviewController extends HttpServlet {
    private  final ReviewService reviewService=new ReviewServiceImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = StringUtil.parseUri(request.getRequestURI());
        Message msg;
        if ("JoinThePlane".equals(requestURI)) {
            //将学习好的模板加入复习计划
           msg = reviewService.joinThePlan(request);
        }else if("RemoveFromPlan".equals(requestURI)){
            //将模板从学习计划中移除
            msg=reviewService.removeFromPlan(request);
        }else if("GetPeriodModle".equals(requestURI)){
            //获取复习周期下的模板
            msg = reviewService.getModleByPeriod(request);
        }else if("FinishOnceReview".equals(requestURI)){
            //完成某一周期的复习
            msg=reviewService.updatePeriod(request);
        }else if("GetUserReviewPlan".equals(requestURI)){
            //获取用户的复习计划列表
            msg=reviewService.getReviewPlan(request);
        }else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
