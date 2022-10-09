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
import utils.ResponseUtil;
import utils.StringUtil;

import java.io.IOException;


@WebServlet("/upload/*")
@MultipartConfig
public class UploadController extends HttpServlet {
    private UserService userService = new UserServiceImpl();
    private ModleService modleService = new ModleServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = StringUtil.parseURI(request.getRequestURI());
        Message msg;
        if ("file".equals(uri)) {
            //上传文件
            msg = userService.setFileById(request);
        } else if ("parseContent".equals(uri)) {
            //获取PDF并解析内容
            msg = modleService.parsePDFContent(request);
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
