package pkserver.listeners;

import pkserver.PkUser;

/**
 * @author yeyeye
 * @Date 2022/10/28 19:04
 */
public interface StatusPoolListener {
    /**
     * 监听匹配完成池新增
     * @param user user
     */
    void matchedPoolAdded(PkUser user);

    /**
     * 监听离开匹配完成池
     */
    void matcherPoolRemoved();

    /**
     * 监听匹配池新增
     * @param user user
     */
    void matchingPoolAdded(PkUser user);

    /**
     * 监听离开匹配池
     */
    void matchingPoolRemoved();

    /**
     * 监听进入取消匹配池
     */
    void cancelMatchingPoolAdded();

    /**
     * 监听离开取消匹配池
     */
    void cancelMatchingPoolRemoved();
}
