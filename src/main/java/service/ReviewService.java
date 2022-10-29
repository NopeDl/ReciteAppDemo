package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.vo.Message;

public interface ReviewService {

    /**
     * 将某个模板加入学习计划
     * @param request
     * @return
     */
    Message joinThePlan(HttpServletRequest request);
}
