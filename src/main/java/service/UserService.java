package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.vo.Message;

public interface UserService {


    /**
     * 注册用户
     *
     * @return
     */
    Message createUser(HttpServletRequest request);


    /***
     * 根据用户id查找UserMsg
     * @param request
     * @return
     */
    Message selectUserMsg(HttpServletRequest request);

    /**
     * 通过传进去的在cookieName遍历cookie,寻找对应value的值
     *
     * @param request
     * @param cookieName
     * @return 找到返回该值，
     */
    String getCookie(HttpServletRequest request, String cookieName);


    /**
     * 根据用户的id修改资料
     *
     * @param userId
     * @param request
     * @return
     */
    Message ReMsgById(int userId, HttpServletRequest request);


    /**
     * 根据id设置头像
     *
     * @param request
     * @return
     */
    @Deprecated
    Message setFileById(HttpServletRequest request);

    /**
     * 检查昵称是否可用
     *
     * @param request
     * @return
     */
    Message checkNickNameExists(HttpServletRequest request);

    /**
     * 获取排行榜前十
     * @param request
     * @return
     */
    Message rankingList(HttpServletRequest request);

    /**
     * 获取用户排位和信息
     * @param request
     * @return
     */
    Message userRanking(HttpServletRequest request);

    /**
     * 打卡
     * @param request
     * @return
     */
    Message clockIn(HttpServletRequest request);

    /**
     *获取打卡记录
     * @param request
     * @return
     */
    Message getClockInRecord(HttpServletRequest request);
}
