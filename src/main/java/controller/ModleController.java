package controller;

import enums.MsgInf;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;
import service.LikesService;
import service.ModleService;
import service.impl.LikesServiceImpl;
import service.impl.ModleServiceImpl;
import tools.utils.Cache;
import tools.utils.ResponseUtil;
import tools.utils.StringUtil;

import java.io.IOException;

/**
 * 执行与模板相关的
 */
@WebServlet("/modle/*")
public class ModleController extends HttpServlet {
    private final ModleService modleService = new ModleServiceImpl();
    private final LikesService likesService = new LikesServiceImpl();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //定时将点赞数量存到数据库里
        //服务器开启前应该先查找对应的数据放到总缓存
        try {
            likesService.initCaChe();
            Cache.tiemrTask();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        //关服务器前关闭缓存并且将缓存内数据存入数据库
        likesService.closeCache();
    }

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
//        } else if ("Study".equals(requestURI)) {
//            //显示模板
//            msg = modleService.reTxt(request);
//        } else if ("reward".equals(requestURI)) {
//            //打赏
//            msg = modleService.reward(request);
        } else if ("toCommunity".equals(requestURI)) {
            //将模板添加至社区
            msg = modleService.toCommunity(request);
        } else if ("UserMemory".equals(requestURI)) {
            //获取用户的记忆库,返回用户的模板状态
            msg = modleService.getUserMemory(request);
        } else if ("autoDig".equals(requestURI)) {
            //自动挖空
            msg = modleService.autoDig(request);
        } else if ("Collection".equals(requestURI)) {
            //用户收藏模板还是取消模板
            msg = modleService.collectModle(request);
        } else if ("UpdateModleStatus".equals(requestURI)) {
            //更新模板的学习状态，只包括：未学习-->学习中 ,已学习-->学习中
            msg=modleService.updateModleStatus(request);
        }else if("LikeOrDisLike".equals(requestURI)){
            //点赞
            msg = likesService.likeOrDisLike(request);
        }else if("SaveStudyRecord".equals(requestURI)){
            //保存学习记录
            msg = modleService.saveRecord(request);
        }else if("GetStudyRecord".equals(requestURI)){
            //获取学习记录
            msg = modleService.showRecord(request);
        }else if("JudgeStudyRecord".equals(requestURI)){
            //判断谋个用户某个模板的学习记录情况，这里将复习的学习记录判断也写在这了
            msg = modleService.judgeStudyRecord(request);
        }else {
            msg = new Message(MsgInf.NOT_FOUND);
        }
        ResponseUtil.send(response, msg);
    }
}
