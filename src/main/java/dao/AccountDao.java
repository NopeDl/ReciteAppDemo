package dao;

import pojo.po.Account;

public interface AccountDao {

    /**
     * 验证账号是否正确
     *
     * @param number   手机号
     * @param password 密码
     * @return 验证成功返回账号对象（对象中包含用户id），否则返回null
     */
    Account selectAccount(String number, String password);


    /**
     * 根据userId修改密码
     *
     * @param userId 用户id
     * @return 成功执行返回1
     */
    int changePasswordByUserId(int userId, String newPassword);

}
