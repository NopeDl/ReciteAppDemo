package service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pojo.vo.Message;

import java.io.InputStream;

public interface ModleService {
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
    String WriteAsTxt(String context, String modleTitle);


    /**
     * 更改原模版路径里面的内容
     *
     * @return
     */
    boolean replaceContext(String context, int modleId);

    /**
     * 解析pdf内容
     *
     * @return 返回pdf中内容
     */
    Message parseFile(HttpServletRequest request);

    /**
     * 获取标签下所有模板
     *
     * @param request
     * @return
     */
    Message getModlesByTag(HttpServletRequest request);


    /**
     * 给模板打赏
     *
     * @param request
     * @return
     */
    Message reward(HttpServletRequest request);


    /**
     * 查询用户的记忆库
     *
     * @param request
     * @return
     */
    Message getUserMemory(HttpServletRequest request);

    /**
     * 获取所有标签信息
     *
     * @return
     */
    Message getLabels();

    /**
     * 自动挖空
     * @return
     */
    Message autoDig(HttpServletRequest request);


    /**
     * 将模板上传至社区
     * @param request
     * @return
     */
    Message toCommunity(HttpServletRequest request);

    /**
     * 更改学习计划
     * @param request
     * @return
     */
    Message updateModleStatus(HttpServletRequest request);


}
