package dao;

import pojo.po.Label;
import pojo.po.Modle;
import pojo.po.Umr;
import pojo.vo.Community;
import pojo.vo.Message;

import java.util.List;

public interface ModleDao {


//    /**
//     * 取消用户收藏
//     * @param userId
//     * @param modleId
//     * @param mStatus
//     * @return
//     */
//    int cancelMOdleById(int userId,int modleId,int mStatus);
//

    /**
     * 收藏模板
     * 讲收藏的模板写进表umr中
     * @return
     */
    int  collectModleById(int userId,int modleId,int mStatus);


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

    /**
     * 删除模板
     * @param modleId
     * @return
     */
    int deleteModle(int modleId);


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
     * @param modleId 需要查找的模板ID
     * @return 模板内容
     */
    Modle selectModleByModleId(int modleId);



    /**
     * 获取标签下所有模板
     * @param modle
     * @return
     */
    List<Community> selectModlesByTag(Modle modle);

    /**
     * 根据modleId来查找相对应的modlePath,modleTitle,modleLabel
     * @param modleId
     * @return
     */
    Modle selectPathTitlAndTag(int modleId);

    String selectPathByModleId(int modleId);

    /**
     * 获取所有标签信息
     * @return 所有标签
     */
    List<Label> selectLabels();

    /**
     * 获取模板id
     *
     * 同一个用户不可以拥有一样标题的模板（有bug，收藏的情况下）
     * @param modle 封装的查找模板信息
     * @return 查到的模板
     */
    Modle selectModleIdByUserIdAndTitle(Modle modle);

    /**
     * 更改模板发布状态
     * @param common 0为不发布，1为发布
     * @param modleId 需要修改的模板ID
     * @return 更改结果
     */
    int updateModleCommon(int modleId,int common);

    /**
     * 根据模板id查询标题
     * @param modle
     * @return
     */
    String selectTitleByModleId(Modle modle);

    /**
     * 更改某个模板的标签
     * @param modle
     * @return
     */
    boolean changeModleTag(Modle modle);


    /**
     * 查看该模板是否属于该用户
     * @param umr
     * @return
     */
    Integer selectIfContain(Umr umr);
}
