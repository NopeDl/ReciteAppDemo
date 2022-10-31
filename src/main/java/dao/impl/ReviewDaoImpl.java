package dao.impl;

import com.sun.org.apache.xpath.internal.operations.Mod;
import dao.ReviewDao;
import pojo.po.db.Modle;
import pojo.po.db.Review;
import pojo.vo.Community;
import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import tools.utils.DaoUtil;

import java.util.ArrayList;
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


    /**
     * 根据模板的复习周期返查找模板,返回一个Community类型的list
     * @param review
     * @return
     */
    @Override
    public List<Community> selectModleByPeriod(Review review) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        List<Community> communities= new ArrayList<>();
        List<Object> objects = sqlSession.selectList("ReviewMapper.selectModleByPeriod", review);
        if(objects.size()>0){
            //查得到返回list<Modle>
            for (int i = 0; i < objects.size(); i++) {
                communities.add((Community) objects.get(i));
            }
        }
        //否则返回null
        return communities;
    }


    /**
     * 查询该模板是否已经加入复习计划
     * @param modle
     * @return
     */
    @Override
    public boolean selectModleIsReview(Modle modle) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        boolean flag=false;
        List<Object> objects = sqlSession.selectList("ReviewMapper.selectModleIsReview", modle);
        if(objects.size()>0){
            flag=true;
        }
        return flag;
    }

    /**
     * 根据模板id查询模板所处的周期
     * @param review
     * @return
     */
    @Override
    public Review selectModlePeriod(Review review) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ReviewMapper.selectModlePeriod", review);
        if(objects.size()>0){
            //成功查询
            return (Review) objects.get(0);
        }
        return null;
    }

    /**
     * 更新周期和日期
     * @param review
     * @return
     */
    @Override
    public int updatePeriodAndDate(Review review) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        int update = sqlSession.update("ReviewMapper.updatePeriodAndDate", review);
        if(update>0){
            sqlSession.commit();
        }else{
            sqlSession.rollBack();
        }
        return update;
    }


    /**
     * 查询用户的复习计划表
     * @param modle
     * @return
     */
    @Override
    public List<Community> selectReviewPlan(Modle modle) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        List<Community> communities=new ArrayList<>();
        List<Object> objects = sqlSession.selectList("ReviewMapper.selectReviewPlan", modle);
        if(objects.size()>0){
            //说明有加入复习的东西
            //存为Community类型的list
            for (int i = 0; i < objects.size(); i++) {
                communities.add((Community) objects.get(i));
            }
        }

        return communities;
    }
}
