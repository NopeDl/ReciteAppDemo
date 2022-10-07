package controller;

import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.AccountService;
import service.UserService;
import service.impl.AccountServiceImpl;
import service.impl.UserServiceImpl;
import utils.ResponseUtil;
import utils.StringUtil;

import java.io.IOException;

/**
 * 信息获取控制器
 */
@WebServlet("/inf.get/*")
public class InfController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = StringUtil.parseURI(request.getRequestURI());
        Message msg;
        if ("checkUsedNumber".equals(uri)) {
            //检查手机号是否存在
            msg = accountService.checkNumberExists(request);
        } else if ("checkUserNickName".equals(uri)) {
            //检查昵称是否存在
            msg = userService.checkNickNameExists(request);
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
