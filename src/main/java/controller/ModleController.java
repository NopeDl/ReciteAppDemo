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
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.IOException;

/**
 * 执行与模板相关的
 */
@WebServlet("/modle/*")
public class ModleController extends HttpServlet {
    private final ModleService modleService = new ModleServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = StringUtil.parseUri(request.getRequestURI());
        Message msg;
        if ("MakeModle".equals(requestURI)) {
            //用户制作模板，三种情况:一种是空模板cv,一种是选择已有的模板再制作，一种是选择本地文件进行创作
            msg = modleService.createModle(request);
        } else if ("deleteModle".equals(requestURI)) {
            //删除模板
            msg = modleService.deleteModle(request);
        } else if ("Study".equals(requestURI)) {
            //显示模板
            msg = modleService.reTxt(request);
        } else if ("reward".equals(requestURI)) {
            //打赏
            msg = modleService.reward(request);
        } else if ("toCommunity".equals(requestURI)) {
            //将模板添加至社区
            msg = modleService.toCommunity(request);
        } else if ("UserMemory".equals(requestURI)) {
            //获取用户的记忆库,返回用户的模板状态
            msg = modleService.getUserMemory(request);
        } else if ("autoDig".equals(requestURI)) {
            //自动挖空
            msg = modleService.autoDig(request);
        } else if("Collection".equals(requestURI)){
            //用户收藏模板还是取消模板
            msg=modleService.collectModle(request);
        }else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
