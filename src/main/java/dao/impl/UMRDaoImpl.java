package dao.impl;

import dao.UMRDao;
import easydao.core.SqlSession;
import easydao.core.SqlSessionFactory;
import pojo.po.Modle;
import pojo.po.Umr;
import utils.DaoUtil;

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
     * @param umr
     * @return
     */
    @Override
    public List<Umr> selectModleByUserId(Umr umr) {
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
     * 保存umr关系
     * @param umr
     * @return
     */
    @Override
    public int insertUMR(Umr umr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int insert = sqlSession.insert("UmrMapper.insertUMR", umr);
        if (insert > 0){
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        return insert;
    }
}
