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

    /**
     * 将模板移除个人计划
     * @param request
     * @return
     */
    Message removeFromPlan(HttpServletRequest request);

    /**
     * 根据周期获取模板
     * @param request
     * @return
     */
    Message getModleByPeriod(HttpServletRequest request);

    /**
     * 将学习完的模板更新周期
     * @param request
     * @return
     */
    Message updatePeriod(HttpServletRequest request);

    /**
     * 获取用户复习计划里的所有内容
     * @param request
     * @return
     */
    Message getReviewPlan(HttpServletRequest request);


}
