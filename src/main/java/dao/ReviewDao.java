package dao;

import pojo.po.db.Modle;
import pojo.po.db.Review;

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
}
