package service.impl;

import dao.AccountDao;
import dao.UserDao;
import dao.impl.AccountDaoImpl;
import dao.impl.UserDaoImpl;
import enums.MsgInf;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.po.Account;
import pojo.vo.Message;
import service.AccountService;

public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao = new AccountDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

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
                msg.addData("userId", account.getUserId());//将userid发送给前端储存

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
     * @param request
     * @return
     */
    @Override
    public Integer getIdByNumber(HttpServletRequest request) {
        String number = request.getParameter("phone");
        Integer userId = accountDao.selectIdByNumber(number);
        return userId;
    }

    /**
     * 检测手机号是否存在
     *
     * @param request
     * @return
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
