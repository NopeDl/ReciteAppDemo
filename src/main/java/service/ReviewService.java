package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.po.db.Review;
import pojo.vo.Message;

import java.util.List;

/**
 * @author ducka
 */
public interface ReviewService {

    /**
     * 将某个模板加入学习计划
     *
     * @param request req
     * @return ret
     */
    Message joinThePlan(HttpServletRequest request);

    /**
     * 将模板移除个人计划
     *
     * @param request req
     * @return ret
     */
    Message removeFromPlan(HttpServletRequest request);

    /**
     * 根据周期获取模板
     *
     * @param request req
     * @return ret
     */
    Message getModleByPeriod(HttpServletRequest request);

    /**
     * 将学习完的模板更新周期
     *
     * @param request req
     * @return ret
     */
    Message updatePeriod(HttpServletRequest request);

//    /**
//     * 获取用户复习计划里的所有内容
//     * @param request
//     * @return
//     */
//    Message getReviewPlan(HttpServletRequest request);

    /**
     * 保存复习的学习记录
     * @param request 获取信息的request
     * @return 返回message的封装对象
     */
    Message saveReviewRecord(HttpServletRequest request);

    /**
     * 获取复习板块的某个用户某个模板的学习记录
     * @param request 获取信息的request
     * @return 返回message
     */
    Message showReviewRecord(HttpServletRequest request);

    /**
     * 当离开复习计划/模板被删除时要将
     * 将复习计划里的存储学习计划的文件删除
     * @param modleId 模板id
     * @param userId 用户id
     * @return 成功返回true 失败返回false
     */
    Boolean deleteRecordPath(int modleId,int userId);


    /**
     * 获取正确率
     *
     * @param request req
     * @return 正确率
     */
    Message getAccuracy(HttpServletRequest request);

}
