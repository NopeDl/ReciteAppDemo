package dao.impl;

import com.sun.org.apache.xpath.internal.operations.Mod;
import dao.ModleDao;
import easydao.core.SqlSession;
import easydao.core.SqlSessionFactory;
import pojo.po.Count;
import pojo.po.File;
import pojo.po.Modle;
import utils.DaoUtil;

import java.util.ArrayList;
import java.util.List;

public class ModleDaoImpl implements ModleDao {
    private final SqlSessionFactory sqlSessionFactory;

    public ModleDaoImpl() {
        sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    }

    @Override
    public int insertFileByUserId(int userId, String path) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        File file = new File();
        file.setUserId(userId);
        file.setFileContext(path);
        int insert = sqlSession.insert("FileMapper.insertFile", file);
        if (insert > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return 0;
    }

    /**
     * 获取文件总个数
     *
     * @return
     */
    @Override
    public Integer selectCount() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> userId = sqlSession.selectList("ModleMapper.selectCount", "userId");
        sqlSession.close();
        if (userId.size() == 0) {
            return 0;
        } else {
            return (((Count) userId.get(0)).getNumber()).intValue();
        }
    }

    /**
     * 统计模板拥有者为xx,模板标题为xxx的个数
     * @param modle
     * @return 返回个数
     */
    @Override
    public int selectNumByTitle(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectNumByTitle", modle);
        sqlSession.close();
        if (objects.size() == 0) {
            return 0;
        } else {
            return (((Count) objects.get(0)).getNumber()).intValue();
        }

    }

//    /**
//     * 更新模板里面的内容
//     * @param modleId
//     * @param modlePath
//     * @return
//     */
//    @Override
//    public int updateMOdle(int modleId, String modlePath) {
//        return 0;
//    }


    /**
     * 插入新的模板
     * @param modle
     * @return
     */
    @Override
    public int insertModle(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int result = sqlSession.insert("ModleMapper.insertModle", modle);

        if(result>0) {
            //插入成功
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return result;
    }

    /**
     * 根据模板Id获取模板数据
     * @param modle
     * @return
     */
    @Override
    public Modle selectModleById(Modle modle) {
        int modleId = modle.getModleId();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> resultList = sqlSession.selectList("ModleMapper.selectModleById", modleId);
        sqlSession.close();
        sqlSession.close();
        if (resultList.size() >0 ){
            return (Modle) resultList.get(0);
        }else {
            return null;
        }
    }

    /**
     * 获取标签下所有模板
     * @param modle
     * @return
     */
    @Override
    public List<Modle> selectModlesByTag(Modle modle) {
        int modleLabel = modle.getModleLabel();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectModlesByTag", modle);
        sqlSession.close();
        if (objects.size()>0){
            List<Modle> modleList = new ArrayList<>();
            for (Object object : objects) {
                Modle ret = (Modle) object;
                modleList.add(ret);
            }
            return modleList;
        }else {
            return null;
        }
    }
}
