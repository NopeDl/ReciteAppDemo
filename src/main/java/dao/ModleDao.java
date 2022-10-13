package dao;

import pojo.po.Modle;

import java.util.List;

public interface ModleDao {

    /**
     * 通过用户id上传文件
     *
     * @param userId
     * @return
     */
    int insertFileByUserId(int userId,String path);


    /**
     *获取总的文件个数
     * @return
     */
    Integer selectCount();

    /**
     * 查找改用户者某个模板标题的个数
     * @param modle
     * @return
     */
    int selectNumByTitle(Modle modle);

//
//    /**
//     * 覆盖原有的模板
//     * @param modle
//     * @return
//     */
//    int updateMOdle(Modle modle);


    /**
     * 插入新的模板
     * @param modle
     * @return
     */
    int insertModle(Modle modle);

    /**
     * 根据模板id查找模板信息
     * @param modle
     * @return
     */
    Modle selectModleById(Modle modle);

    /**
     * 获取标签下所有模板
     * @param modle
     * @return
     */
    List<Modle> selectModlesByTag(Modle modle);

}
