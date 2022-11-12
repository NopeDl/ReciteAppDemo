package dao;

import pojo.po.db.Modle;
import pojo.po.db.Umr;

import java.util.List;

public interface UMRDao {
    /**
     * 根据umr里面的userId的来查找所对应的modleid
     * @param userId 用户ID
     * @return
     */
    List<Umr> selectModleByUserId(int userId);

    /**
     * 根据传进来的modle里面的属性modleId来查找相对应的信息
     * @param umr
     * @return
     */
    Modle selectModleByModleId(Umr umr);

    /**
     * 保存umr关系
     * @param umr
     * @return
     */
    int insertUMR(Umr umr);

    /**
     * 删除umr关系
     * @param umr
     * @return
     */
    int deleteUMRByModleId(Umr umr);

    /**
     * 根据传进来的userId和modleId查看某个模板是否被已被收藏
     * @param umr umr
     * @return r
     */
    Integer slelectIfCollect(Umr umr);
}
