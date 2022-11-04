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
        config.setClearStopwords(true);
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

    /**'
     * 字数统计
     * @param content 需要统计的内容
     * @return 返回字数
     */
    public static int wordCount(String content) {
        if (content == null) {
            return 0;
        }
        String englishString = content.replaceAll("[\u4e00-\u9fa5]", "");
        String[] englishWords = englishString.split("[\\p{P}\\p{S}\\p{Z}\\s]+");
        int chineseWordCount = content.length() - englishString.length();
        int otherWordCount = englishWords.length;
        if (englishWords.length > 0 && englishWords[0].length() < 1) {
            otherWordCount--;
        }
        if (englishWords.length > 1 && englishWords[englishWords.length - 1].length() < 1) {
            otherWordCount--;
        }
        return chineseWordCount + otherWordCount;
    }
}
