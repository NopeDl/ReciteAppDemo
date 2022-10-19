package dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DateDao {
    /**
     * 打卡
     * @param userId
     * @return
     */
    int insertDateByUserId(int userId, LocalDate date);

    /**
     * 查询打卡记录
     * @param userId
     * @return
     */
    List<String> selectDateByUserId(int userId,String month,String year);
}
