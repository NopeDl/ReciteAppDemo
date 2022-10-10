package service.impl;

import easydao.utils.Resources;
import jakarta.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import pojo.vo.Message;
import service.PageService;

import java.io.InputStream;
import java.util.List;

public class PageServiceImpl implements PageService {
    /**
     * 获取页面名字
     *
     * @param request
     * @return 页面
     */
    @Override
    public Message getPage(HttpServletRequest request) {
        String pageName = request.getParameter("pageName");
        InputStream input = Resources.getResourceAsStream("static/page/" + pageName);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(input);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            Element body = null;
            for (Element element : elements) {
                if ("body".equals(element.getName())){
                    body = element;
                    break;
                }
            }

            if (body !=null ){
                elements = body.elements("div");
                StringBuilder sb = new StringBuilder();
                for (Element element : elements) {
                    sb.append(element.asXML());
                }
                String ret = sb.toString().replaceAll("\"", "'");
                Message msg = new Message("文档读取成功");
                msg.addData("page",ret);
                return msg;
            }
        } catch (DocumentException e) {
            throw new RuntimeException("文档读取失败！");
        }
        return new Message("文档读取失败");
    }
}
