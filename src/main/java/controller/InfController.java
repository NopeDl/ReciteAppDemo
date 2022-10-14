package controller;

import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.AccountService;
import service.ModleService;
import service.PageService;
import service.UserService;
import service.impl.AccountServiceImpl;
import service.impl.ModleServiceImpl;
import service.impl.PageServiceImpl;
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
    private final ModleService modleService = new ModleServiceImpl();
    private final PageService pageService = new PageServiceImpl();

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
        } else if ("getModlesByTag".equals(uri)) {
            //获取标签下所有模板
            msg = modleService.getModlesByTag(request);
        } else if ("getLabels".equals(uri)) {
            msg = modleService.getLabels();
        } else if ("getPage".equals(uri)) {
            //获取页面
            //暂时弃用
            msg = pageService.getPage(request);
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
