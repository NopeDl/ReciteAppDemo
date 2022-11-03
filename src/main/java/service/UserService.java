package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.vo.Message;

public interface UserService {
    /**
     * 退出登录
     * @param request r
     * @return r
     */
    Message quit(HttpServletRequest request);


    /**
     * 注册用户
     *
     * @return r
     */
    Message createUser(HttpServletRequest request);


    /***
     * 根据用户id查找UserMsg
     * @param request r
     * @return r
     */
    Message selectUserMsg(HttpServletRequest request);


    /**
     * 根据用户的id修改资料
     *
     * @param request r
     * @return r
     */
    Message ReMsgById(HttpServletRequest request);

    /**
     * 检查昵称是否可用
     *
     * @param request r
     * @return r
     */
    Message checkNickNameExists(HttpServletRequest request);

    /**
     * 获取排行榜前十
     * @param request r
     * @return r
     */
    Message rankingList(HttpServletRequest request);

    /**
     * 获取用户排位和信息
     * @param request r
     * @return r
     */
    Message userRanking(HttpServletRequest request);

    /**
     * 打卡
     * @param request 1
     * @return 1
     */
    Message clockIn(HttpServletRequest request);

    /**
     *获取打卡记录
     * @param request r
     * @return r
     */
    Message getClockInRecord(HttpServletRequest request);

    /**
     * 将用户的头像的base64形式存在
     * @param base64 b
     * @param useId u
     * @return r
     */
    String WriteImageAsTxt(String base64, int useId);

    /**
     * 存头像
     * @param request r
     * @return r
     */
    Message saveImg(HttpServletRequest request);

    /**
     * 存日常学习数据： 学习篇数和学习时长
     * @param request req
     * @return msg
     */
    Message saveDailyData(HttpServletRequest request);

    /**
     * 获取用户日常学习信息： 学习篇数和学习时长
     * @param request req
     * @return msg
     */
    Message getUserDailyStudyData(HttpServletRequest request);
}
