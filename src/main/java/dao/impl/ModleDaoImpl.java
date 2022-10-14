package dao.impl;

import dao.ModleDao;
import easydao.core.SqlSession;
import easydao.core.SqlSessionFactory;
import pojo.po.*;
import pojo.vo.Message;
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
     * 修改模板打赏量
     * @param modle
     * @return
     */
    @Override
    public int updateModleCoins(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int update = sqlSession.update("ModleMapper.updateCoinsByModleId", modle);
        if (update > 0){
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return 0;
    }

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
    public Modle selectModleByModleId(Modle modle) {
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


    /**
     * 根据umr里面的userI的来查找所对应的modleid
     * @param umr
     * @return
     */
    @Override
    public Umr[] selectModleByUserId(Umr umr) {

        Umr[] umrs;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("UmrMapper.selectModleByUserId", umr);
        sqlSession.close();
        if(objects.size()==0){
            umrs=new Umr[0];
        }else{
            umrs=new Umr[objects.size()];

        }

        return umrs;
    }


    /**
     * 根据传过来的modleId来查找modle信息
     * @param umr
     * @return
     */
    @Override
    public Modle selectModleByModleId(Umr umr) {
        Modle modle;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectModleByModleId", umr);
        sqlSession.close();
        if(objects.size()==0){
            modle=null;
        }else{
          modle= (Modle) objects.get(0);
        }
        return modle;
    }

    /***
     * 根据模板对象的id号查找相对于的模板路径
     * @param modleId
     * @return
     */
    @Override
    public String selectPathByModleId(int modleId) {
        Modle modle=new Modle();
        modle.setModleId(modleId);

        String modlePath=null;//传回去的值
        SqlSession sqlSession=sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectPathByModleId", modle);
        sqlSession.close();
        if(objects.size()!=0){
           //有路径
            modlePath= (String) objects.get(0);
        }
        return modlePath;
    }

    @Override
    public List<Label> selectLabels() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> labelList = sqlSession.selectList("ModleMapper.selectLables", "ss");
        sqlSession.close();
        if (labelList.size()>0){
            //如果查到则转换成po
            List<Label> labelsList = new ArrayList<>();
            for (Object o : labelList) {
                labelsList.add((Label) o);
            }
            return labelsList;
        }else {
            return null;
        }

    }
}
