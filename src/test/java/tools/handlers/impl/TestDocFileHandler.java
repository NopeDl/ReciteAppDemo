package tools.handlers.impl;

import org.junit.Test;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author yeyeye
 * @Date 2022/10/19 1:55
 */
public class TestDocFileHandler {
    /**
     * 测试解析doc文档
     */
    @Test
    public void parseContentTest() throws Exception{
        String path = ClassLoader.getSystemClassLoader().getResource("test.docx").getPath().substring(1);
        InputStream input = new FileInputStream(path);
        FileHandler docHandler = FileHandlerFactory.getHandler("docx", input);
        System.out.println(docHandler.parseContent());
    }
}
