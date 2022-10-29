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
}
