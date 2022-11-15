package dao.impl;

import dao.ModleDao;
import pojo.po.db.*;
import pojo.vo.Community;
import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import tools.utils.DaoUtil;

import java.util.ArrayList;
import java.util.List;

public class ModleDaoImpl implements ModleDao {
    private final SqlSessionFactory sqlSessionFactory;

    public ModleDaoImpl() {
        sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    }


//    /**
//     *  查询模板是否属于该用户
//     * @param umr
//     * @return
//     */
//    @Override
//    public boolean ifModleBelongUser(Umr umr) {
//        SqlSession sqlSession=sqlSessionFactory.openSession();
//        boolean flag=false;
//        List<Object> objects = sqlSession.selectList("UmrMapper.ifModleBelongUser", umr);
//        if(objects.size()>0){
//            //说明用户存在该模板
//            flag=true;
//        }
//        return flag;
//    }


    @Override
    public boolean updateReviewId(Review review) {
        boolean flag = false;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int update = sqlSession.update("ModleMapper.updateReviewId", review);
        if (update > 0) {
            flag = true;
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }

        sqlSession.close();
        return flag;
    }

    /**
     * 收藏前先查看用户要收藏的模板是不是自己模板
     * 在modle表查询
     *
     * @param umr
     * @return
     */
    @Override
    public Integer selectIfContain(Umr umr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> userId = sqlSession.selectList("ModleMapper.selectIfContain", umr);
        sqlSession.close();
        if (userId.size() == 0) {
            //说明此时不属于该用户
            return 0;
        } else {
            //说明此时属于用户自己的模板
            return (((Count) userId.get(0)).getNumber()).intValue();
        }
    }


    /**
     * 获取某个模板的标题是叫什么
     *
     * @param modle
     * @return
     */
    @Override
    public String selectTitleByModleId(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        String userTitle = null;
        List<Object> objects = sqlSession.selectList("ModleMapper.selectTitleByModleId", modle);
        sqlSession.close();
        if (objects.size() == 0) {
            //说明没有叫这个标题的模板
        } else {
            userTitle = ((Modle) objects.get(0)).getModleTitle();
        }
        return userTitle;

    }

//    /**
//     * 用户取消收藏的模板
//     * @param userId
//     * @param modleId
//     * @param mStatus
//     * @return
//     */
//    @Override
//    public int cancelMOdleById(int userId, int modleId, int mStatus) {
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        Umr umr = new Umr();
//        umr.setUserId(userId);
//        umr.setModleId(modleId);//将数据封装为一个umr对象
//        umr.setMStatus(mStatus);
//        int result = sqlSession.delete("UmrMapper.cancelMOdleById", umr);
//        if(result>0){
//            sqlSession.commit();
//        }else{
//            sqlSession.rollBack();
//        }
//
//        sqlSession.close();
//        return result;
//    }

    /**
     * 更新模板的学习状态
     *
     * @param umr
     * @return
     */
    @Override
    public int updateStudyStatus(Umr umr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int update = sqlSession.insert("UmrMapper.updateStudyStatus", umr);
        if (update > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return update;
    }

    /**
     * 用户收藏别人的模板
     *
     * @return
     */
    @Override
    public int collectModleById(int userId, int modleId, int mStatus) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Umr umr = new Umr();
        umr.setUserId(userId);
        umr.setModleId(modleId);//将数据封装为一个umr对象
        umr.setMStatus(mStatus);
        int update = sqlSession.insert("UmrMapper.collectModleById", umr);
        if (update > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return update;


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
     *
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
     *
     * @param modle
     * @return
     */
    @Override
    public int updateModleCoins(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int update = sqlSession.update("ModleMapper.updateCoinsByModleId", modle);
        if (update > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return 0;
    }

    /**
     * 插入新的模板
     *
     * @param modle
     * @return
     */
    @Override
    public int insertModle(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int result = sqlSession.insert("ModleMapper.insertModle", modle);

        if (result > 0) {
            //插入成功
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return result;
    }

    /**
     * 根据模板Id获取模板数据
     *
     * @param modleId 需要查找的模板ID
     * @return 模板数据
     */
    @Override
    public synchronized Modle selectModleByModleId(int modleId) {
        Modle modle = new Modle();
        modle.setModleId(modleId);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> resultList = sqlSession.selectList("ModleMapper.selectModleById", modle);
        sqlSession.close();
        if (resultList.size() > 0) {
            System.out.println((Modle) resultList.get(0));
            return (Modle) resultList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取标签下所有模板
     *
     * @param modle
     * @return
     */
    @Override
    public List<Community> selectModlesByTag(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectModlesByTag", modle);
        sqlSession.close();
        List<Community> modleList = new ArrayList<>();
        if (objects.size() > 0) {
//            List<Modle> modleList = new ArrayList<>();
            for (Object object : objects) {
                Community ret = (Community) object;
//                ret.setModlePath(null);
                modleList.add(ret);

            }
            return modleList;
        } else {
            return modleList;
        }
    }


    /***
     * 根据模板对象的id号查找相对于的模板路径
     * @param modleId
     * @return
     */
    @Override
    public Modle selectPathTitlAndTag(int modleId) {
        Modle modle = new Modle();
        modle.setModleId(modleId);

        //返回modle1
        Modle modle1;
        //传回去的值
        String modlePath = null;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectPathByModleId", modle);
        sqlSession.close();
        if (objects.size() != 0) {
            //有路径
//            modlePath = ((Modle)objects.get(0)).getModlePath();
            //返回路径，标题，标签，封装成modle
            modle1 = (Modle) objects.get(0);
        } else {
            modle1 = null;
        }
        return modle1;
    }

    @Override
    public List<Label> selectLabels() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> labelList = sqlSession.selectList("ModleMapper.selectLables", "ss");
        sqlSession.close();
        if (labelList.size() > 0) {
            //如果查到则转换成po
            List<Label> labelsList = new ArrayList<>();
            for (Object o : labelList) {
                labelsList.add((Label) o);
            }
            return labelsList;
        } else {
            return null;
        }

    }


    /**
     * 获取模板id
     * <p>
     * 同一个用户不可以拥有一样标题的模板（有bug，收藏的情况下）
     *
     * @param modle
     * @return
     */
    @Override
    public Modle selectModleIdByUserIdAndTitle(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> modleList = sqlSession.selectList("ModleMapper.selectModleId", modle);
//        List<Object> modleList = sqlSession.selectList("ModleMapper.selectModleIdByUserIdAndTitle", modle);
        sqlSession.close();
        return modleList.size() > 0 ? (Modle) modleList.get(0) : null;
    }

    @Override
    public boolean changeModleTag(Modle modle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int update = sqlSession.update("ModleMapper.changeModleTag", modle);
        boolean flag = false;
        if (update > 0) {
            flag = true;
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
//        System.out.println("flag="+flag);
        return flag;
    }


    @Override
    public String selectPathByModleId(int modleId) {
        Modle modle = new Modle();
        modle.setModleId(modleId);
        //传回去的值
        String modlePath = null;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectPathByModleId", modle);
        sqlSession.close();
        if (objects.size() != 0) {
            //有路径
            modlePath = ((Modle) objects.get(0)).getModlePath();

        }
        return modlePath;
    }

    /**
     * 删除模板
     *
     * @param modleId 需要删除的模板ID
     * @return 操作结果
     */
    @Override
    public int deleteModle(int modleId) {
        Modle modle = new Modle();
        modle.setModleId(modleId);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int delete = sqlSession.delete("ModleMapper.deleteModleByModleId", modle);
        if (delete > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return delete;
    }

    /**
     * 上传模板
     *
     * @param modleId 需要操作的模板ID
     * @param common  0为不发布，1为发布
     * @return 更改结果
     */
    @Override
    public int updateModleCommon(int modleId, int common) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Modle modle = new Modle();
        modle.setModleId(modleId);
        modle.setCommon(common);
        int update = sqlSession.update("ModleMapper.updateModleCommon", modle);
        if (update > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return update;
    }

    /**
     * 根据模板id和用户id查询模板的情况，重点包括模板的学习状态
     *
     * @param umr
     * @return
     */
    @Override
    public Umr selectModleByIds(Umr umr) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("UmrMapper.selectModleByIds", umr);
        sqlSession.close();
        if (objects.size() > 0) {
            return (Umr) objects.get(0);
        }
        return null;
    }

    /**
     * 更新模板的点赞数量
     *
     * @param modleId
     * @return
     */
    @Override
    public int updateLikeNum(int modleId, int great) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Modle modle = new Modle();
        modle.setModleId(modleId);
        modle.setGreat(great);
        int update = sqlSession.update("ModleMapper.UpdateLikeNum", modle);
        if (update > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return update;
    }

    /**
     * 获取随机模板
     *
     * @param modleLabel 模板标签
     * @return 随机模板
     */
    @Override
    public List<Community> selectRandomModles(int modleLabel) {
        Modle modle = new Modle();
        modle.setModleLabel(modleLabel);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectRandomModles", modle);
        sqlSession.close();
        if (objects.size() <= 0) {
            return null;
        } else {
            List<Community> modleList = new ArrayList<>();
            for (Object o : objects) {
                modleList.add((Community) o);
            }
            return modleList;
        }

    }

    /**
     * 查询用户所有已上传模板
     *
     * @param userId 用户ID
     * @return 模板
     */
    @Override
    public List<Community> selectModleByUserId(int userId, int pageIndex) {
        Modle modle = new Modle();
        modle.setUserId(userId);
        modle.setPageIndex(pageIndex);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectModleByUserId", modle);
        sqlSession.close();
        if (objects.size() <= 0) {
            return null;
        } else {
            List<Community> list = new ArrayList<>();
            objects.forEach((o) -> {
                list.add((Community) o);
            });
            return list;
        }
    }

    @Override
    public List<Community> selectRandomModlesByUserId(int userId) {
        Modle modle = new Modle();
        modle.setUserId(userId);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectRandomModlesByUserId", modle);
        sqlSession.close();
        if (objects.size() <= 0) {
            return null;
        } else {
            List<Community> list = new ArrayList<>();
            objects.forEach((o) -> {
                list.add((Community) o);
            });
            return list;
        }
    }

    @Override
    public List<Community> selectHotModles(int pageIndex) {
        Modle modle = new Modle();
        modle.setPageIndex(pageIndex);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("ModleMapper.selectHotModle", modle);
        sqlSession.close();
        if (objects.size() > 0){
            List<Community> list = new ArrayList<>();
            objects.forEach((o) -> {
                list.add((Community) o);
            });
            return list;
        }
        return null;
    }
}
