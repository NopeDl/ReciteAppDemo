package dao.impl;

import dao.DateDao;
import easydao.core.SqlSession;
import easydao.core.SqlSessionFactory;
import pojo.po.UDate;
import pojo.po.User;
import utils.DaoUtil;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateDaoImpl implements DateDao {
    private final SqlSessionFactory sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    /**
     * 打卡
     * @param userId
     * @return
     */
    @Override
    public int insertDateByUserId(int userId,Date date) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UDate uDate = new UDate();
        uDate.setDate(date);
        uDate.setUserId(userId);
        int insert = sqlSession.insert("DateMapper.insertDate", uDate);
        if (insert>0){
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return insert;
    }

    /**
     * 查询用户所有打卡日期
     * @param userId
     * @return
     */
    @Override
    public List<String> selectDateByUserId(int userId) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setUserId(userId);
        List<Object> objects = sqlSession.selectList("DateMapper.selectDates", user);
        sqlSession.close();
        if (objects.size()>0){
            List<String> dateList = new ArrayList<>();
            for (Object object : objects) {
                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(((UDate) object).getDate());
                dateList.add(date);
            }
            return dateList;
        }else {
            return null;
        }
    }
}
