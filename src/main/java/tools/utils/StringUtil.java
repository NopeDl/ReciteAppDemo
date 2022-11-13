package tools.utils;

import com.alibaba.fastjson.JSONObject;
import dao.impl.ModleDaoImpl;
import enums.Difficulty;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StringUtil {
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
     * @param modleId  模板ID
     * @param blankNum 空数
     * @return 已经挖好空的模板
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
        //去除用户挖空
        content = content.replaceAll("<(?!br).*?>", "").replaceAll("<br>", "\n").trim();
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
                //有bug
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
        return result.toString().replaceAll("\\n", "<br>");
    }


    /**
     * 解析文件中的自定义括号
     *
     * @param content 需要挖空的内容
     * @return 挖好空的内容
     */
    public static String parseQuote(String content) {

        return null;
    }

    /**
     * 答案相似度匹配
     *
     * @param json json
     * @return 匹配度
     */
    public static String stringMatch(JSONObject json) {
        if (json != null) {
            Map<String, String> strMap = new HashMap<>();
            //将参考答案和输入数据放入集合中
            json.forEach((key, value) -> {
                strMap.put(key, (String) value);
            });
            //计算所有空最短编辑距离和
            AtomicInteger simRatio = new AtomicInteger(0);
            strMap.forEach((ref, ans) -> {
                //计算参考答案和回答的最短编辑距离
                double minDis = calDistance(ref, ans);
                double rate = (ref.length() - minDis) / ref.length();
                simRatio.getAndAdd(new Long(Math.round(rate * 100)).intValue());
            });
            //相似度
            int r = simRatio.get() / strMap.size();
            if (r > 0) {
                return r + "%";
            }
        }
        return "0%";
    }

    /**
     * 计算最短编辑距离
     *
     * @param str1 串1
     * @param str2 串2
     * @return 距离
     */
    public static int calDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        //创建dp数组
        int[][] dp = new int[len1 + 1][len2 + 1];
        //初始化数组
        for (int i = 0; i <= len2; i++) {
            dp[0][i] = i;
        }
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        //开始计算
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    //如果最后一个字符相等
                    dp[i][j] = Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), dp[i - 1][j - 1] - 1) + 1;
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        return dp[len1][len2];
    }

}
