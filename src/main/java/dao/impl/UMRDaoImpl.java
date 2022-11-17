package dao.impl;

import dao.UMRDao;
import pojo.po.db.Count;
import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import pojo.po.db.Modle;
import pojo.po.db.Umr;
import tools.utils.DaoUtil;

import java.util.ArrayList;
import java.util.List;

public class UMRDaoImpl implements UMRDao {

    private final SqlSessionFactory sqlSessionFactory;

    public UMRDaoImpl() {
        this.sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    }

    /**
     * 根据umr里面的userI的来查找所对应的modleid
     *
     * @param userId
     * @return
     */
    @Override
    public List<Umr> selectModleByUserId(int userId) {
        Umr umr = new Umr();
        umr.setUserId(userId);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("UmrMapper.selectModleByUserId", umr);
        sqlSession.close();
        if (objects.size() == 0) {
            return null;
        } else {
            List<Umr> umrList = new ArrayList<>();
            for (Object object : objects) {
                umrList.add((Umr) object);
            }
            return umrList;
        }
    }


    /**
     * 根据传过来的modleId来查找modle信息
     *
     * @param umr
     * @return
     */
    @Override
    public Modle selectModleByModleId(Umr umr) {
        Modle modle;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectModleByModleId", umr);
        sqlSession.close();
        if (objects.size() == 0) {
            modle = null;
        } else {
            modle = (Modle) objects.get(0);
        }
        return modle;
    }

    /**
     *
     * @param userId 用户id
     * @param modleId 模板id
     * @param mStatus 收藏状态
     * @param recordPath 存储学习记录的路径
     * @return 插入成功返回1，
     */
    @Override
    public int insertUMR(int userId,int modleId,int mStatus,String recordPath) {
        Umr umr=new Umr();
        umr.setUserId(userId);
        umr.setModleId(modleId);
        umr.setMStatus(mStatus);
        umr.setRecordPath(recordPath);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int insert = sqlSession.insert("UmrMapper.insertUMR", umr);
        if (insert > 0){
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        return insert;
    }

    /**
     * 删除umr关系
     * @param umr
     * @return
     */
    @Override
    public int deleteUMRByModleId(Umr umr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int delete = sqlSession.delete("UmrMapper.deleteUMRByModleId",umr);
        if (delete>0){
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return delete;
    }

    /**
     * 根据传进来的userId和modleId查看某个模板是否被已被收藏
     * 返回true,说明用户有收藏，否则没有收藏
     * @param umr
     * @return
     */
    @Override
    public Integer slelectIfCollect(Umr umr) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        List<Object> userId = sqlSession.selectList("UmrMapper.slelectIfCollect", umr);
        sqlSession.close();
        if(userId.size()==0){
            //说明此时没有有结果
            //umr中没有信息,说明没有收藏
            return 0;
        }else{
            //说明有收藏
            return (((Count) userId.get(0)).getNumber()).intValue();
        }
    }

    /**
     * 获取学习记录的文件路径
     * @param modleId 要保存学习记录的模板id
     * @param userId 用户的id
     * @return 返回保存的文件路径
     */
    @Override
    public String selectRecordPath(int modleId,int userId) {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        Umr umr=new Umr();
        umr.setUserId(userId);
        umr.setModleId(modleId);
        String recordPath="";
        List<Object> objects = sqlSession.selectList("UmrMapper.selectRecordPath", umr);
        sqlSession.close();
        if(objects.size()>0){
            //说明查得到数据
            recordPath=((Umr)objects.get(0)).getRecordPath();
        }
        return recordPath;
    }

    /**
     * 查询所有与modleId有关的umr关系
     * @param modleId 相关的模板id
     * @return 返回list集合的umr对象
     */
    @Override
    public List<Umr> selectUmrByModleId(int modleId) {
        Umr umr=new Umr();
        umr.setModleId(modleId);
        List<Umr> umrList = new ArrayList<>();
        SqlSession sqlSession=sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("UmrMapper.selectUmrByModleId", umr);
        if(objects.size()>0){
            for (Object object : objects) {
                umrList.add((Umr) object);
            }
        }
        return umrList;
    }
}
