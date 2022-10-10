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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PageServiceImpl implements PageService {
//    /**
//     * 获取页面名字
//     *
//     * @param request
//     * @return 页面
//     */
//    @Override
//    public Message getPage(HttpServletRequest request) {
//        String pageName = request.getParameter("pageName");
//        InputStream input = Resources.getResourceAsStream("static/page/" + pageName);
//        SAXReader saxReader = new SAXReader();
//        try {
//            Document document = saxReader.read(input);
//            Element root = document.getRootElement();
//            List<Element> elements = root.elements();
//            Element body = null;
//            for (Element element : elements) {
//                if ("body".equals(element.getName())){
//                    body = element;
//                    break;
//                }
//            }
//
//            if (body !=null ){
//                elements = body.elements("div");
//                StringBuilder sb = new StringBuilder();
//                for (Element element : elements) {
//                    sb.append(element.asXML());
//                }
//                String ret = sb.toString().replaceAll("\"", "'");
//                Message msg = new Message("文档读取成功");
//                msg.addData("page",ret);
//                return msg;
//            }
//        } catch (DocumentException e) {
//            throw new RuntimeException("文档读取失败！");
//        }
//        return new Message("文档读取失败");
//    }


    /**
     * 获取预渲染页面
     * @param request
     * @return
     */
    @Override
    public Message getPage(HttpServletRequest request) {
        //获取页面对应储存文件路径
        String pageName = request.getParameter("pageName");
        pageName = pageName.substring(0,pageName.lastIndexOf(".")) + ".txt";
        String path = Resources.getResource("static/page/" + pageName);
        File file = new File(path);
        if (!file.exists()){
            return new Message("页面不存在");
        }
        InputStream input = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            //读取文件内容
            input = new FileInputStream(file);
            reader = new InputStreamReader(input, "UTF-8");
            br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine())!=null){
                sb.append(line);
            }
            //处理字符串
            String ret = sb.toString().replaceAll("\"", "'");
            ret = ret.replaceAll("\r\n", " ");
            //封装并响应
            Message msg = new Message("页面获取成功");
            msg.addData("page",ret);
            return msg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            //释放资源
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
