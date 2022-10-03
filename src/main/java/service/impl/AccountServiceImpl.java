package service.impl;

import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import enums.MsgInf;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.po.Account;
import pojo.vo.Message;
import service.AccountService;

public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao = new AccountDaoImpl();

    /**
     * 根据手机号和密码验证账户
     */
    @Override
    public Message<?> checkAccount(HttpServletRequest request, HttpServletResponse response) {
        String number = request.getParameter("phone");
        String password = request.getParameter("password");
        Account account = accountDao.selectAccount(number, password);
        Message<Object> msg;
        if (account == null) {
            //验证信息不通过
            msg = new Message<>(MsgInf.UNAUTHORIZED);
        } else {
            //验证成功
            msg = new Message<>();
            //发送userId cookie给用户
            Cookie cookie = new Cookie("userId", account.getUserId() + "");
            response.addCookie(cookie);
        }
        return msg;
    }
}
