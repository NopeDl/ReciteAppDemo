package dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 日期
 * @author h2012
 */
public interface DateDao {
    /**
     * 打卡
     * @param userId 打卡的用户ID
     * @param date 日期
     * @return 数字
     */
    int insertDateByUserId(int userId);

    /**
     * 查询打卡记录
     * @param userId 打卡的用户ID
     * @return 条数
     */
    List<String> selectDateByUserId(int userId,String month,String year);
}
