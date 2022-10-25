package tools.handlers.impl;

import org.apache.lucene.analysis.Analyzer;
import org.junit.Test;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.extractor.SummaryExtractor;
import org.lionsoul.jcseg.extractor.impl.TextRankKeyphraseExtractor;
import org.lionsoul.jcseg.extractor.impl.TextRankSummaryExtractor;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.lionsoul.jcseg.sentence.SentenceSeg;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

/**
 * @author yeyeye
 * @Date 2022/10/19 1:55
 */
public class TestDocFileHandler {
    /**
     * 测试解析doc文档
     */
    @Test
    public void parseContentTest() throws Exception {
        String path = ClassLoader.getSystemClassLoader().getResource("test.docx").getPath().substring(1);
        InputStream input = new FileInputStream(path);
        FileHandler docHandler = FileHandlerFactory.getHandler("docx", input);
        System.out.println(docHandler.parseContent());
    }

    @Test
    public void testJcseg() throws Exception {
        SegmenterConfig config = new SegmenterConfig();
        ADictionary dictionary = DictionaryFactory.createSingletonDictionary(config);
        ISegment segment = ISegment.COMPLEX.factory.create(config, dictionary);

        String s = "你好，你是世界上最可爱的人";
        segment.reset(new StringReader(s));
        IWord iWord = null;
        while ((iWord = segment.next()) != null) {
            System.out.println(iWord.getValue());
        }
    }

    @Test
    public void testKeyWord() throws Exception {
        SegmenterConfig config = new SegmenterConfig();
        ADictionary dictionary = DictionaryFactory.createSingletonDictionary(config);
        ISegment segment = ISegment.COMPLEX.factory.create(config, dictionary);
        TextRankKeyphraseExtractor extractor = new TextRankKeyphraseExtractor(segment);
        extractor.setKeywordsNum(15);           //设置最大返回的关键词个数，默认为10
        extractor.setMaxWordsNum(4);
        String str = "节约用水，从点滴做起。也许有人会认为一个小时 内集到 3.6公斤水，1个月内集到 2.6吨水。这些水量，足以可以给予一个人半 年的生活用水所需。可见，一点一滴水的浪费都是不应该的。但只有我们拯救， 也许微不足道。但如果我不救任何人，只会带来更糟糕的结果。所以不要给自己 不节水的理由。相信大家都见过一些水资源匮乏的地方，日子很难过。我们大多 数人都生活在这种看似正常的生活环境中。但是，对于某些地方来说，这种生活 是奢侈的。而如果我们不节约水资源，总有一天，大部分人类都会到那个地步。 3月 22日是世界水日。每年的这一天，世界各国都会宣传：大家要爱惜水资 源，保护生命之水。其实，不光在这一天，在我们生命中的每一天，都应该珍惜 每一滴水，用实际行动珍爱生命之水!水，天上、地下、江湖海里都有。对于节 约用水，同学们总觉得不如节约别的东西那么重要。有的同学开水龙头、洗脚、 洗毛笔，有同学不喝开水，而是嘴对着水龙头喝自来水，水声哗哗，一点也不心 疼。\n";
        List<String> keyphrase = extractor.getKeyphrase(new StringReader(str));
        for (String s : keyphrase) {
            System.out.println(s);
        }

    }

    @Test
    public void testSentence() throws Exception {
//        SegmenterConfig config = new SegmenterConfig();
//        ADictionary dictionary = DictionaryFactory.createSingletonDictionary(config);
//        ISegment segment = ISegment.COMPLEX.factory.create(config, dictionary);
//        SummaryExtractor extractor = new TextRankSummaryExtractor(segment,new SentenceSeg())

        //1, 创建Jcseg ISegment分词对象
        SegmenterConfig config = new SegmenterConfig(true);
        config.setClearStopwords(true);     //设置过滤停止词
        config.setAppendCJKSyn(false);      //设置关闭同义词追加
        config.setKeepUnregWords(false);    //设置去除不识别的词条
        ADictionary dic = DictionaryFactory.createSingletonDictionary(config);
        ISegment seg = ISegment.COMPLEX.factory.create(config, dic);

//2, 构造TextRankSummaryExtractor自动摘要提取对象
        SummaryExtractor extractor = new TextRankSummaryExtractor(seg, new SentenceSeg());


//3, 从一个Reader输入流中获取length长度的摘要
        String str = "Jcseg是基于mmseg算法的一个轻量级开源中文分词器，同时集成了关键字提取，关键短语提取，关键句子提取和文章自动摘要等功能，并且提供了最新版本的lucene,%20solr,%20elasticsearch的分词接口。Jcseg自带了一个%20jcseg.properties文件用于快速配置而得到适合不同场合的分词应用。例如：最大匹配词长，是否开启中文人名识别，是否追加拼音，是否追加同义词等！";
        String summary = extractor.getSummary(new StringReader(str), 128);
        System.out.println(summary);

//4, output:
//Jcseg是基于mmseg算法的一个轻量级开源中文分词器，同时集成了关键字提取，关键短语提取，关键句子提取和文章自动摘要等功能，并且提供了最新版本的lucene, solr, elasticsearch的分词接口。


//-----------------------------------------------------------------
//5, 从一个Reader输入流中提取n个关键句子
        str = "you source string here";
        List<String> keySentences = extractor.getKeySentence(new StringReader(str));
        for (String keySentence : keySentences) {
            System.out.println(keySentence);
        }

    }


}
