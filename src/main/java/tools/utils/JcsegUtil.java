package tools.utils;

import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yeyeye
 * @Date 2022/10/25 12:20
 */
public class JcsegUtil {
    private static final ISegment SEGMENT;

    static {
        SegmenterConfig config = new SegmenterConfig();
        ADictionary dictionary = DictionaryFactory.createSingletonDictionary(config);
        SEGMENT = ISegment.COMPLEX.factory.create(config,dictionary);
    }

    /**
     * 获取分词结果
     * @param content 需要分词的内容
     * @return 分词结果，封装在List集合中
     */
    public static List<String> getSegments(String content){
        //分词实现
        List<String> res = new ArrayList<>();
        try {
            SEGMENT.reset(new StringReader(content));
            IWord iWord;
            while ((iWord = SEGMENT.next())!=null){
                res.add(iWord.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //若分词失败则返回NULL
        return res;
    }
}
