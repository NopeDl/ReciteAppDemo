package dao;

import com.sun.org.apache.xpath.internal.operations.Mod;
import pojo.po.Label;
import pojo.po.Modle;
import pojo.po.Umr;

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
     * 修改模板打赏数
     * @param modle
     * @return
     */
    int updateModleCoins(Modle modle);



    /**
     * 根据模板id查找模板信息
     * @param modle
     * @return
     */
    Modle selectModleByModleId(Modle modle);



    /**
     * 获取标签下所有模板
     * @param modle
     * @return
     */
    List<Modle> selectModlesByTag(Modle modle);

//    /**
//     * 通过用户userId来查找用户有的模板
//     * @param userId
//     * @return
//     */
//    String[] slectModleByUserId(int userId);



    /**
     * 根据modleId来查找相对应的modlePath,modleTitle,modleLabel
     * @param modleId
     * @return
     */
    Modle selectPathTitlAndTag(int modleId);

    String selectPathByModleId(int modleId);

    /**
     * 获取所有标签信息
     * @return
     */
    List<Label> selectLabels();

    /**
     * 获取模板id
     *
     * 同一个用户不可以拥有一样标题的模板（有bug，收藏的情况下）
     * @param modle
     * @return
     */
    Modle selectModleIdByUserIdAndTitle(Modle modle);

}
