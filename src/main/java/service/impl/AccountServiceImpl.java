package service.impl;

import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import enums.MsgInf;
import pojo.po.Account;
import pojo.vo.Message;
import service.AccountService;

public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao = new AccountDaoImpl();

    /**
     * 根据手机号和密码验证账户
     *
     * @param number
     * @param password
     * @return
     */
    @Override
    public Message<?> checkAccount(String number, String password) {
        Account account = accountDao.selectAccount(number, password);
        Message<Object> msg;
        if (account == null) {
            //验证信息不通过
            msg = new Message<>(MsgInf.UNAUTHORIZED);
        } else {
            //验证成功
            msg = new Message<>();
        }
        return msg;
    }
}
