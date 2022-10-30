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
        }else if("removeFromPlan".equals(requestURI)){
            //将模板从学习计划
            msg=reviewService.removeFromPlan(request);
        }else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
