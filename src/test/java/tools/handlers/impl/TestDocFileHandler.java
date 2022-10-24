package tools.handlers.impl;

import org.apache.lucene.analysis.Analyzer;
import org.junit.Test;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;

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

    @Test
    public void testJcseg() throws Exception {
        SegmenterConfig config = new SegmenterConfig();
        ADictionary dictionary = DictionaryFactory.createSingletonDictionary(config);
        ISegment segment = ISegment.COMPLEX.factory.create(config,dictionary);

        String s = "你好，我是爸爸";
        segment.reset(new StringReader(s));
        IWord iWord = null;
        while ((iWord =segment.next())!=null){
            System.out.println(iWord.getValue());
        }
    }
}
