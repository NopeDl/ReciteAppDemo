package service;

import pojo.po.User;
import pojo.vo.Message;

public interface UserService {
    /**
     * 根据用户的id号查找该用户的个人资料
     * @param userId
     * @return 返回user
     */
    User getMsgById(int userId);


}
