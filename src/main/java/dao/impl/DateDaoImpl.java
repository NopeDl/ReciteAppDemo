package dao.impl;

import dao.DateDao;
import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import pojo.po.UDate;
import pojo.po.User;
import tools.utils.DaoUtil;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateDaoImpl implements DateDao {
    private final SqlSessionFactory sqlSessionFactory = DaoUtil.getSqlSessionFactory();

    /**
     * 打卡
     *
     * @param userId
     * @return
     */
    @Override
    public int  insertDateByUserId(int userId, LocalDate date) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UDate uDate = new UDate();
        uDate.setDate(date);
        uDate.setUserId(userId);
        int insert = sqlSession.insert("DateMapper.insertDate", uDate);
        if (insert > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return insert;
    }

    /**
     * 查询用户所有打卡日期
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> selectDateByUserId(int userId, String month, String year) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UDate uDate = new UDate();
        uDate.setUserId(userId);
        uDate.setYear(year);
        uDate.setMonth(month);
        //补上前导0
        month = String.format("%2s", month).replaceAll(" ", "0");
        uDate.setExp(year + "-" + month + "%");
        List<Object> objects = sqlSession.selectList("DateMapper.selectDates", uDate);
        sqlSession.close();
        if (objects.size() > 0) {
            //好搞笑
            System.out.println("我是真的大于0");
            List<String> dateList = new ArrayList<>();
            for (Object object : objects) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = ((UDate) object).getDate();
                String date = formatter.format(localDate);
                dateList.add(date);
            }
            return dateList;
        } else {
            return null;
        }
    }
}
