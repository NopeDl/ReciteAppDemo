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
     * 根据用户的id修改资料
     *
     * @param request
     * @return
     */
    Message ReMsgById(HttpServletRequest request);

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

    /**
     * 将用户的头像的base64形式存在
     * @param base64
     * @param useId
     * @return
     */
    String WriteImageAsTxt(String base64, int useId);
}
