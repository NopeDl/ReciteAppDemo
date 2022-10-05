package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.po.User;
import pojo.vo.Message;

public interface UserService {


    /**
     * 注册用户
     *
     * @return
     */
    Message<?> createUser(HttpServletRequest request);


    /***
     * 根据用户id查找UserMsg
     * @param request
     * @return
     */
    Message selectUserMsg(HttpServletRequest request);

    /**
     * 通过传进去的在cookieName遍历cookie,寻找对应value的值
     * @param request
     * @param cookieName
     * @return 找到返回该值，
     */
    String getCookie(HttpServletRequest request,String cookieName);


    /**
     * 根据用户的id修改资料
     * @param userId
     * @param request
     * @return
     */
    Message ReMsgById(int userId,HttpServletRequest request);
}
