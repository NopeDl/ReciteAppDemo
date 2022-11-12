package service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import enums.MsgInf;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.po.db.Account;
import pojo.vo.Message;
import service.AccountService;
import tools.utils.JwtUtil;

import java.util.Calendar;
import java.util.HashMap;

public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao = new AccountDaoImpl();

    /**
     * 根据手机号和密码验证账户
     */
    @Override
    public Message checkAccount(HttpServletRequest request, HttpServletResponse response) {
        String number = request.getParameter("phone");
        String password = request.getParameter("password");
        Message msg;
        if (number != null && password != null) {
            //参数不为空
            Account account = accountDao.selectAccount(number, password);
            if (account == null) {
                //验证信息不通过
                msg = new Message(MsgInf.UNAUTHORIZED);
                msg.addData("isSuccess", false);
            } else {
                //验证成功
                msg = new Message("登陆成功");
                msg.addData("isSuccess", true);
                //将userid发送给前端储存
//                msg.addData("userId", account.getUserId());
                //发送token
                String token = JwtUtil.getInstance(account.getUserId());
                msg.addData("token", token);
                Cookie cookie = new Cookie("token", token);
                response.addCookie(cookie);
//                登录成功后再session中设置用户id
//                request.getSession().setAttribute("userId", account.getUserId());
            }
        } else {
            msg = new Message("phone或者password参数不能为空");
            msg.addData("isSuccess", false);
        }
        return msg;
    }

    /**
     * 根据手机号获取id
     *
     * @param request req
     * @return ret
     */
    @Override
    public Integer getIdByNumber(HttpServletRequest request) {
        String number = request.getParameter("phone");
        return accountDao.selectIdByNumber(number);
    }

    /**
     * 检测手机号是否存在
     *
     * @param request req
     * @return ret
     */
    @Override
    public Message checkNumberExists(HttpServletRequest request) {
        Message msg;
        Integer id = this.getIdByNumber(request);
        if (id == null) {
            //手机号没被使用过
            msg = new Message("该手机号可以使用");
            msg.addData("isOk", true);
        } else {
            //手机号被使用过
            msg = new Message("该手机号已被注册");
            msg.addData("isOk", false);
        }
        return msg;
    }
}
