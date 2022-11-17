package service;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pojo.vo.Message;

import java.io.InputStream;

public interface ModleService {
    /**
     * 获取热门模板
     * @param request 请求
     * @return 热门模板
     */
    Message getHotModle(HttpServletRequest request);

    /**
     * 收藏模板
     * @param request
     * @return
     */
     Message collectModle(HttpServletRequest request);


    /**
     * 创作模板
     *
     * @param request
     * @return
     */
    Message createModle(HttpServletRequest request);

    /**
     * 删除模板
     * @param request
     * @return
     */
    Message deleteModle(HttpServletRequest request);


    /**
     * 读取txt文本里里面的string
     *
     * @param request
     * @return
     */
    Message reTxt(HttpServletRequest request);


    /***
     * 将String类型的字符串存为txt文本，并且返回文件的地址
     * @param context
     * @param modleTitle
     * @return
     */
    String writeAsTxt(String context, String modleTitle);


    /**
     * 更改原模版路径里面的内容
     * @param path 文件路径
     * @param context context
     * @return ret
     */
    boolean replaceContext(String context, String path);

    /**
     * 解析pdf内容
     * @param request req
     * @return 返回pdf中内容
     */
    Message parseFile(HttpServletRequest request);

    /**
     * 获取标签下所有模板
     *
     * @param request req
     * @return ret
     */
    Message getModlesByTag(HttpServletRequest request);

    /**
     * 查询用户的记忆库
     *
     * @param request req
     * @return ret
     */
    Message getUserMemory(HttpServletRequest request);

    /**
     * 获取所有标签信息
     *
     * @return ret
     */
    Message getLabels();

    /**
     * 自动挖空
     * @return ret
     */
    Message autoDig(HttpServletRequest request);


    /**
     * 将模板上传至社区
     * @param request req
     * @return ret
     */
    Message toCommunity(HttpServletRequest request);

    /**
     * 更改学习计划
     * @param request req
     * @return ret
     */
    Message updateModleStatus(HttpServletRequest request);

    /**
     *  下拉获取随机模板
     * @param request req
     * @return 随机模板
     */
    Message getRandomModles(HttpServletRequest request);


    /**
     * 保存学习记录
     * @param request 用来获取所需要的信息
     * @return 返回Message封装的信息
     */
    Message saveRecord(HttpServletRequest request);


    /**
     * 获取上一次的学习记录
     * @param request 用来获取模板id
     * @return
     */
    Message showRecord(HttpServletRequest request);

    /**
     * 判断是否学习
     * @param path 路径
     * @return 有返回true,否则返回false
     */
    boolean judgeIfRecord(String path);


    /**
     * 判断是否有学习记录
     * @param request 获取传入的modleId
     * @return 返回message类
     */
    Message judgeStudyRecord(HttpServletRequest request);
}
