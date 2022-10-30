package dao.impl;

import dao.ReviewDao;
import pojo.po.db.Modle;
import pojo.po.db.Review;
import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import tools.utils.DaoUtil;

import java.util.List;


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

    /**
     * 从计划表中移除模板
     * @param review
     * @return
     */
    @Override
    public int removeModle(Review review) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        int delete = sqlSession.delete("ReviewMapper.removeModle", review);
        if(delete>0){
            //删除成功
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return delete;
    }

    /**
     * 查询计划表中是否存在某个模板
     * @param review
     * @return
     */
    @Override
    public boolean selectModle(Review review) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        boolean flag=false;
        List<Object> objects = sqlSession.selectList("ReviewMapper.selectModle", review);
        sqlSession.close();
        if(objects.size()>0){
            //此时存在
            flag=true;
        }
        return flag;
    }
}
