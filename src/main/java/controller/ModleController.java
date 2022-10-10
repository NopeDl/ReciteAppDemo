package controller;

import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.ModleService;
import service.impl.ModleServiceImpl;
import utils.StringUtil;

import java.io.IOException;

/**
 * 执行与模板相关的
 */
@WebServlet("/modle/*")
public class ModleController extends HttpServlet {
    private final ModleService ModleService = new ModleServiceImpl();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = StringUtil.parseURI(request.getRequestURI());
        Message msg;
//        if ("UpLoadFile".equals(requestURI)) {
//            //用户选择上传文件,上传文件只是想要将文件的内容获取，服务端并没有一直保存pdf文件
//            msg = ModleService.UpLoad(request);//有bug，前端响应头可能过大无法正常发送（提供UploadController中的parseContent接口解决）
//        } else
        if ("MakeModle".equals(requestURI)) {
            //用户制作模板，三种情况:一种是空模板cv,一种是选择已有的模板再制作，一种是选择本地文件进行创作
            msg = ModleService.createModle(request);
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }

    }
}
