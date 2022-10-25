package controller;

import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.ModleService;
import service.UserService;
import service.impl.ModleServiceImpl;
import service.impl.UserServiceImpl;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.*;


@WebServlet("/upload/*")
@MultipartConfig
public class UploadController extends HttpServlet {
    private final ModleService modleService = new ModleServiceImpl();

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = StringUtil.parseUri(request.getRequestURI());
        Message msg;
        if ("parseContent".equals(uri)) {
            //获取PDF并解析内容
            msg = modleService.parseFile(request);
        } else if ("uploadImg".equals(uri)) {
            //设置头像
            msg = userService.saveImg(request);
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
