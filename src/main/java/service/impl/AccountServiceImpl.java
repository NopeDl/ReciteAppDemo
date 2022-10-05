package service.impl;

import com.mysql.cj.Session;
import dao.AccountDao;
import dao.UserDao;
import dao.impl.AccountDaoImpl;
import dao.impl.UserDaoImpl;
import enums.MsgInf;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pojo.po.Account;
import pojo.po.User;
import pojo.vo.Message;
import service.AccountService;

import javax.persistence.criteria.CriteriaBuilder;

public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao = new AccountDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    /**
     * 根据手机号和密码验证账户
     */
    @Override
    public Message<?> checkAccount(HttpServletRequest request, HttpServletResponse response) {
        String number = request.getParameter("phone");
        String password = request.getParameter("password");
        Message<Object> msg;
        if (number != null && password != null) {
            //参数不为空
            Account account = accountDao.selectAccount(number, password);
            if (account == null) {
                //验证信息不通过
                msg = new Message<>(MsgInf.UNAUTHORIZED);
            } else {
                //验证成功
                msg = new Message<>();
//                //发送userId cookie给用户
//                Cookie cookie = new Cookie("userId", account.getUserId() + "");
//                response.addCookie(cookie);

//                !!! new version !!!
//                登录成功后再session中设置用户id
                request.getSession().setAttribute("userId", account.getUserId());
            }
        } else {
            msg = new Message<>("phone或者password参数不能为空");
        }
        return msg;
    }

    @Override
    public Message<?> changePassword(HttpServletRequest request) {
        int userId = (int) request.getSession().getAttribute("userId");
        String newPassword = request.getParameter("password");
        int i = accountDao.changePasswordByUserId(userId, newPassword);
        Message<?> message;
        if (i > 0) {
            message = new Message<>("密码修改成功", "true");
        } else {
            message = new Message<>("密码修改失败", "false");
        }
        return message;
    }


    @Override
    public Integer getIdByNumber(HttpServletRequest request) {
        String number = request.getParameter("phone");
        Integer userId = accountDao.selectIdByNumber(number);
        return userId;
    }
}
