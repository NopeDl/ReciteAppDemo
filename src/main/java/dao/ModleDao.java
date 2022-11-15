package dao;

import jakarta.servlet.http.HttpServletRequest;
import pojo.po.db.Label;
import pojo.po.db.Modle;
import pojo.po.db.Review;
import pojo.po.db.Umr;
import pojo.vo.Community;

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
     * 更新umr表的reviewId字段
     * @param review
     * @return
     */
    boolean updateReviewId(Review review);

    /**
     * 更新模板学习状态
     * @param umr
     * @return
     */
    int updateStudyStatus(Umr umr);


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
     * @param modleId 模板id
     * @return  返回模板标题
     */
    String selectTitleByModleId(int modleId);

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

//
//    /**
//     * 查询模板是否属于用户
//     * @param umr
//     * @return
//     */
//    boolean ifModleBelongUser(Umr umr);

    /**
     * 根据模板的id和用户的id在umr表中查询模板的情况
     * @param umr
     * @return
     */
    Umr selectModleByIds(Umr umr);

    /**
     * 更新点赞数量
     * @param modleId
     * @return
     */
    int updateLikeNum(int modleId,int great);

    /**
     * 获取随机模板
     * @param modleLabel 模板标签
     * @return 随机模板
     */
    List<Community> selectRandomModles(int modleLabel);

    /**
     * 查询用户所有已上传模板
     * @param userId 用户ID
     * @param pageIndex 查询分页
     * @return 模板
     */
    List<Community> selectModleByUserId(int userId,int pageIndex);

    /**
     * 获取随机用户上传模板
     * @param userId 用户I
     * @return 随机模板
     */
    List<Community> selectRandomModlesByUserId(int userId);

    /**
     * 查询热门模板
     * @param pageIndex 分页下标
     * @return 返回值
     */
    List<Community> selectHotModles(int pageIndex);
}
