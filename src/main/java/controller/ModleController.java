package controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.ModleService;
import service.impl.ModleServiceImpl;
import utils.ResponseUtil;
import utils.StringUtil;

import java.io.IOException;

/**
 * 执行与模板相关的
 */
@WebServlet("/modle/*")
public class ModleController extends HttpServlet {
    private final ModleService modleService = new ModleServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = StringUtil.parseURI(request.getRequestURI());
        Message msg;
        if ("MakeModle".equals(requestURI)) {
            //用户制作模板，三种情况:一种是空模板cv,一种是选择已有的模板再制作，一种是选择本地文件进行创作
            msg = modleService.createModle(request);
        }else if("study".equals(requestURI)){
            //显示模板
            msg= modleService.reTxt(request);
        } else if ("reward".equals(requestURI)) {
            //打赏
            msg = modleService.reward(request);
        } else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
