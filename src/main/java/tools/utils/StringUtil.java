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
import java.util.Random;

public class StringUtil {
    /**
     * 获取子串个数
     * @param s1
     * @param s2
     * @return
     */
    public static int subStrCount(String s1,String s2) {
        int index,count=0;
        if(!s1.contains(s2)){
            return 0;
        }
        index=s1.indexOf(s2);
        while (index!=-1) {
            count++;
            index=s1.indexOf(s2,index+1);//方法一：使用indexOf方法；

            //s1=s1.substring(index+s2.length());//方法二：使用subString方法
            //index=s1.indexOf(s2);
        }
        return count;
    }

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

    public static String handleParagraph(String content){
        //将换行转换为<p>标签
        //将开头首句添加<p>
        content = content.replaceAll("\\r\\n", "</p><p>");
        content = "<p>" + content;
        content = content + "</p>";
        return content;
    }


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
            return content;
            /// 禁用自动挖空，有bug
//            //去除所有用户自己挖空内容
//            //需要优化太耗时
//            content = content.replaceAll("<div>", "").replaceAll("</div>", "");
//
//            //根据模板字符数确定要挖的字数
//            int charNum = (int) Math.round(content.length() * ratio);
//            //计算需要挖的空数
//            //假定要挖的空为  charNum * ratio
//            int blankNum = getBlankNumByContentLength(charNum,ratio);
//            //挖空
//            List<Integer> charNums = new ArrayList<>();
//            Random random = new Random(System.currentTimeMillis());
//            for (int i = 0; i < blankNum; i++) {
//                charNums.add(random.nextInt(10) + 1);
//            }
//             return StringUtil.digBlank(content, charNums, blankNum);
        }catch (IOException e){
            throw new RuntimeException("获取" + path + "流失败");
        }
    }

    /**
     * 获取挖空数量
     * @param charNum 文章字数
     * @param ratio 挖空比例
     * @return 挖空数量
     */
    public static int getBlankNumByContentLength(int charNum,double ratio){
        return (int) Math.round(charNum * ratio);
    }

    /**
     * 自动挖空实现
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
     * 获取非零随机数
     *
     * @return 非零随机数
     */
    private static int getRandomInt(int bound) {
        Random random = new Random(System.currentTimeMillis());
        int ret;
        while ((ret = random.nextInt(bound)) == 0) {
        }
        return ret;
    }
}
