package dao.impl;

import dao.LikesDao;
import pojo.po.db.Likes;
import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import tools.utils.DaoUtil;

import java.util.LinkedList;
import java.util.List;

public class LikesDaoImp implements LikesDao {
    private final SqlSessionFactory sqlSessionFactory;

    public LikesDaoImp() {
        this.sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    }


    /**
     * 查询like表里的内容
     * @return
     */
    @Override
    public List<Likes> selectLikes() {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        List<Likes> likes=new LinkedList<>();
        List<Object> objects = sqlSession.selectList("LikesMapper.selectLikes", " ");
        if(objects.size()>0){
            //说明查得到数据
            for (int i = 0; i < objects.size(); i++) {
                likes.add((Likes)objects.get(i));
            }
        }
        return likes;
    }

    /**
     * 更新like表里面的内容
     * @return
     */
    @Override
    public int insetIntoLikes(int userId,int modleId) {
        Likes likes=new Likes();
        likes.setUserId(userId);
        likes.setModleId(modleId);

        SqlSession sqlSession=sqlSessionFactory.openSession();
        int insert = sqlSession.insert("LikesMapper.insetIntoLikes", likes);
        if(insert>0){
            sqlSession.commit();
        }else{
            sqlSession.rollBack();
        }
        return insert;
    }

    /**
     * 删除取消点赞的userId
     * @param userId
     * @return
     */
    @Override
    public int deleteLikes(int userId,int modleId) {
        Likes likes=new Likes();
        likes.setModleId(modleId);
        likes.setUserId(userId);
        SqlSession sqlSession=sqlSessionFactory.openSession();
        int delete = sqlSession.delete("LikesMapper.deleteLikes", likes);
        if(delete>0){
            sqlSession.commit();
        }else{
            sqlSession.rollBack();
        }
        return delete;
    }

    /**
     * 查询用户对模板的点赞情况
     * @param userId 用户id
     * @param modleId 社区的帖子
     * @return 点过赞返回true，否则返回false
     */
    @Override
    public boolean selectifUserLike(int userId, int modleId) {
        Likes likes=new Likes();
        likes.setModleId(modleId);
        likes.setUserId(userId);
        boolean isLike=false;
        SqlSession sqlSession=sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("LikesMapper.selectifUserLike", likes);
        sqlSession.close();
        if(objects.size()>0){
            //说明有点赞过
            isLike=true;
        }

        return isLike;
    }
}
