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
import service.impl.ModleServiceImpl;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.*;


@WebServlet("/upload/*")
@MultipartConfig
public class UploadController extends HttpServlet {
    private ModleService modleService = new ModleServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = StringUtil.parseURI(request.getRequestURI());
        Message msg;
        if ("parseContent".equals(uri)) {
            //获取PDF并解析内容
            msg = modleService.parseFile(request);
        } else if ("uploadImg".equals(uri)) {
            msg = null;
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
