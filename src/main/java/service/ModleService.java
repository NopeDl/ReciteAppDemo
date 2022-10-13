package service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.vo.Message;

import java.io.InputStream;

public interface ModleService {



    /**
     * 用户上传文件，这里应该还要返回pdf文件里面的文字内容
     * @return
     */
    Message UpLoad(HttpServletRequest request);


    /**
     * 将base64转化为pdf
     * @param input
     * @return
     */
    String getPdfPath(InputStream input, String pdfFile);

    /**
     * 将pdf内容提取,返回文本内容
     * @param url

     */
    String rePdf(String url);

    /**
     * 创作模板
     * @param request
     * @return
     */
    Message createModle(HttpServletRequest request);


    /**
     *读取txt文本里里面的string
     * @param request
     * @return
     */
    Message reTxt(HttpServletRequest request);


    /***
     * 将String类型的字符串存为txt文本，并且返回文件的地址
     * @param context
     * @param modleId
     * @return
     */
    String WriteAsTxt(String context,int modleId);


    /**
     * 更改原模版路径里面的内容
     * @return
     */
    boolean replaceContext(String context,int modleId);

    /**
     * 解析pdf内容
     * @return 返回pdf中内容
     */
    Message parsePDFContent(HttpServletRequest request);

    /**
     * 获取标签下所有模板
     * @param request
     * @return
     */
    Message getModlesByTag(HttpServletRequest request);


    /**
     * 给模板打赏
     * @param request
     * @return
     */
    Message reward(HttpServletRequest request);

}
