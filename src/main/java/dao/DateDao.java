package dao;

import java.util.Date;
import java.util.List;

public interface DateDao {
    /**
     * 打卡
     * @param userId
     * @return
     */
    int insertDateByUserId(int userId, Date date);

    /**
     * 查询打卡记录
     * @param userId
     * @return
     */
    List<String> selectDateByUserId(int userId,String month,String year);
}
