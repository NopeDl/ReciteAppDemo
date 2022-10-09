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
import service.UserService;
import service.impl.AccountServiceImpl;
import service.impl.ModleServiceImpl;
import service.impl.UserServiceImpl;
import utils.ResponseUtil;
import utils.StringUtil;

import java.io.IOException;

/**
 * 执行用户相关操作
 */
@WebServlet("/user.do/*")
public class UserController extends HttpServlet {
    private final AccountService accountService = new AccountServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final ModleService ModleService = new ModleServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取URI
        String requestURI = StringUtil.parseURI(request.getRequestURI());
        //根据URI类型执行对应方法
        Message msg = null;
        if ("Login".equals(requestURI)) {
            //登录
            msg = accountService.checkAccount(request, response);
        } else if ("Reg".equals(requestURI)) {
            //注册
            msg = userService.createUser(request);
        } else if ("UserMsg".equals(requestURI)) {
            //用户个人信息获取
            msg = userService.selectUserMsg(request);
        } else if ("ChangePswd".equals(requestURI)) {
            //修改密码
            msg = accountService.changePassword(request);
        } else if ("ReMessage".equals(requestURI)) {
            //修改个人信息
            Integer userId = accountService.getIdByNumber(request);
            msg = userService.ReMsgById(userId, request);
        } else if ("UpLoadFile".equals(requestURI)) {
            //用户选择上传文件,上传文件只是想要将文件的内容获取，服务端并没有一直保存pdf文件
            msg = ModleService.UpLoad(request);//有bug，前端响应头可能过大无法正常发送（提供UploadController中的parseContent接口解决）
        } else if ("MakeModle".equals(requestURI)) {
            //用户制作模板，三种情况:一种是空模板cv,一种是选择已有的模板再制作，一种是选择本地文件进行创作
            msg = ModleService.createModle(request);
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        //发送响应消息体
        ResponseUtil.send(response, msg);

    }
}
