package tools.utils;

import dao.ModleDao;
import dao.impl.ModleDaoImpl;
import enums.Difficulty;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringUtil {
    /**
     * 获取子串个数
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int subStrCount(String s1, String s2) {
        int index, count = 0;
        if (!s1.contains(s2)) {
            return 0;
        }
        index = s1.indexOf(s2);
        while (index != -1) {
            count++;
            index = s1.indexOf(s2, index + 1);
        }
        return count;
    }

    /**
     * 获取uri
     *
     * @param url 需要解析的url
     * @return uri
     */
    public static String parseUri(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 获取临时URL
     *
     * @param fileName
     * @return
     */
    public static String getTempUrl(String fileName) {
        return (StringUtil.class.getClassLoader().getResource("/").getPath() + fileName + ".pdf").substring(1);
    }

    /**
     * 段落处理
     *
     * @param content 内容
     * @return 处理好段落的内容
     */
    public static String handleParagraph(String content) {
        //将换行转换为<p>标签
        //将开头首句添加<p>
        content = content.replaceAll("\\r\\n", "</p><p>");
        content = "<p>" + content;
        content = content + "</p>";
        return content;
    }

    /**
     * 自动挖空
     *
     * @param modleId    需要挖空的模板ID
     * @param difficulty 难度
     * @return 挖好空的模板内容
     */
    public static String autoDig(int modleId, Difficulty difficulty) {
        //获取挖空比例
        double ratio = difficulty.getRatio();
        //查询模板地址
        String path = (new ModleDaoImpl()).selectPathByModleId(modleId);
        //根据地址获取文件字节输入流
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(path));
            //获取文件内容
            FileHandler txtHandler = FileHandlerFactory.getHandler("txt", inputStream);
            String content = txtHandler.parseContent();
            //去除用户挖空
            content = content.replaceAll("<(?!br).*?>", "").replaceAll("<br>", "\n").trim();
            //统计文本字数
            int charNums = content.length();
            //获取挖空数量
            int blankNum = getBlankNumByContentLength(charNums, ratio);
            //开挖
            content = digBlank(content, blankNum);
            return content;
        } catch (IOException e) {
            throw new RuntimeException("获取" + path + "流失败");
        }
    }

    /**
     * 已知模板数量直接挖
     *
     * @param modleId
     * @param blankNum
     * @return
     */
    public static String autoDig(int modleId, int blankNum) {
        //查询模板地址
        String path = (new ModleDaoImpl()).selectPathByModleId(modleId);
        //根据地址获取文件字节输入流
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(path));
            //获取文件内容
            FileHandler txtHandler = FileHandlerFactory.getHandler("txt", inputStream);
            String content = txtHandler.parseContent();
            //去除用户挖空
            content = content.replaceAll("<(?!br).*?>", "").replaceAll("<br>", "\n").trim();
            //统计文本字数
            int charNums = content.length();
            //开挖
            content = digBlank(content, blankNum);
            return content;
        } catch (IOException e) {
            throw new RuntimeException("获取" + path + "流失败");
        }
    }

    /**
     * 获取挖空数量
     *
     * @param charNum 文章字数
     * @param ratio   挖空比例
     * @return 挖空数量
     */
    public static int getBlankNumByContentLength(int charNum, double ratio) {
        return (int) Math.round(charNum * ratio) / 7;
    }

    /**
     * 自动挖空实现
     *
     * @param content  挖空的内容
     * @param blankNum 需要挖空的数量
     * @return 挖好的内容(div)
     */
    public static synchronized String digBlank(String content, int blankNum) {
        //将文本根据空数均匀分段
        //获取每段长度
        int step = content.length() / blankNum;
        //分段
        List<String> strList = new ArrayList<>();
        int beginIndex;
        for (beginIndex = 0; beginIndex + step < content.length(); beginIndex += step) {
            strList.add(content.substring(beginIndex, beginIndex + step));
        }
        //最后一部分也要加进去
        strList.add(content.substring(beginIndex));
        //中文分词
        //记录分词后结果
        StringBuilder result = new StringBuilder();
        for (String src : strList) {
            //获取分词结果
            List<String> segments = JcsegUtil.getSegments(src);
            if (segments.size() != 0) {
                //如果该子串挖空失败则循环至挖空成功为止
                boolean isDig = false;
                while (!isDig) {
                    //随机获取一个不是标点符号分词
                    Random random = new Random(System.currentTimeMillis());
                    int randomIndex = random.nextInt(segments.size());
                    String word = segments.get(randomIndex);
                    int cnt = 0;
                    while (word.length() == 1 && cnt < 10) {
                        randomIndex = random.nextInt(segments.size());
                        word = segments.get(randomIndex);
                        //限制次数，防止死循环
                        cnt++;
                    }
                    //将分的词左右加上<div></div>
                    StringBuilder sb = new StringBuilder(src);
                    int begin = sb.indexOf(word);
                    if (begin == -1) {
                        //没找到
                        //跳过本轮
                        continue;
                    }
                    //找的到就挖
                    sb.insert(begin, "<div>");
                    begin = sb.indexOf(word) + word.length();
                    sb.insert(begin, "</div>");
                    //拼接
                    result.append(sb);
                    //该子串挖空成功
                    isDig = true;
                }
            } else {
                //如果没有分出词则直接拼回去
                result.append(src);
            }
        }
        return result.toString().replaceAll("\n","<br>");
    }
}
