package pkserver.listeners;

import pkserver.PkUser;

/**
 * @author yeyeye
 * @Date 2022/10/28 19:04
 */
public interface StatusPoolListener {
    /**
     * 监听pk池新增
     */
    void pkPoolAdded(PkUser user);

    /**
     * 监听离开pk池
     */
    void pkPoolRemoved();

    /**
     * 监听匹配完成池新增
     */
    void matchedPoolAdded(PkUser user);

    /**
     * 监听离开匹配完成池
     */
    void matcherPoolRemoved();

    /**
     * 监听匹配池新增
     */
    void matchingPoolAdded(PkUser user);

    /**
     * 监听离开匹配池
     */
    void matchingPoolRemoved();
}
