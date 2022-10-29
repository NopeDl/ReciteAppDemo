package dao.impl;

import dao.ReviewDao;
import pojo.po.db.Modle;
import pojo.po.db.Review;
import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import tools.utils.DaoUtil;


public class ReviewDaoImpl implements ReviewDao {

    private final SqlSessionFactory sqlSessionFactory ;

    public ReviewDaoImpl() {
        sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    }


    /**
     * 插入复习计划表
     * @param review
     * @return
     */
    @Override
    public int joinIntoPlan(Review review) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int insert = sqlSession.insert("ReviewMapper.joinIntoPlan", review);
        if(insert>0){
            sqlSession.commit();
        }else{
            sqlSession.rollBack();
        }
        sqlSession.close();
       return insert;
    }
}
