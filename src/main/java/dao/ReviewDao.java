package dao;

import pojo.po.db.Modle;
import pojo.po.db.Review;
import pojo.vo.Community;

import java.util.List;

/**
 * 复习表
 */
public interface ReviewDao {
    /**
     * 将要复习的模板写进review表中
     * @param review
     * @return
     */
    int joinIntoPlan(Review review);


    /**
     * 将模板从计划表中移除
     * @param review
     * @return
     */
    int removeModle(Review review);

    /**
     * 查询计划表中是否存在该模板
     * @param review
     * @return
     */
    boolean selectModle(Review review);

    /**
     * 根据复习周期查询返回的模板
     * @param review
     * @return
     */
    List<Community> selectModleByPeriod(Review review);

    /**
     * 查询该模板是否已经加入复习计划
     * @param modle
     * @return
     */
    boolean selectModleIsReview(Modle modle);

    /**
     *根据模板id查询模板的周期情况
     * @param review
     * @return
     */
    Review selectModlePeriod(Review review);

    /**
     * 更新周期，以及注册日期
     * @param review
     * @return
     */
    int updatePeriodAndDate(Review review);

    /**
     * 查询复习计划表
     * @param modle
     * @return
     */
    List<Community> selectReviewPlan(Modle modle);
}
