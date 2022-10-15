package utils;

import java.util.List;
import java.util.Random;

public class StringUtil {
    /**
     * 获取uri
     *
     * @param url 需要解析的url
     * @return uri
     */
    public static String parseURI(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static String getTempURL(String fileName) {
        return (StringUtil.class.getClassLoader().getResource("/").getPath() + fileName + ".pdf").substring(1);
    }

    /**
     * 自动挖空
     *
     * @param content  挖空的内容
     * @param charNum  每个空里面的字数（数组）
     * @param blankNum 需要挖空的数量
     * @return 挖好的内容(div)
     */
    public static String digBlank(String content, List<Integer> charNum, int blankNum) {
        StringBuilder sb = new StringBuilder(content);
        //两个空之间的最大步长
        int step = content.length() / blankNum;

        //从某一个位置开始
        int beginIndex = step / blankNum;
        int endIndex = beginIndex + 6 + charNum.get(0);
        for (int i = 1; i < charNum.size() && endIndex < sb.length(); i++) {
            sb.insert(beginIndex, "<div>");
            sb.insert(endIndex, "</div>");
            beginIndex = endIndex + 6 + getRandomInt(step);
            endIndex = beginIndex + 6 + charNum.get(i);
        }
        return sb.toString();
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private static int getRandomInt(int bound) {
        Random random = new Random(System.currentTimeMillis());
        int ret;
        while ((ret = random.nextInt(bound)) == 0) {
        }
        return ret;
    }
}
