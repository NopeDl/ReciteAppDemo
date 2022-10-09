package easydao.utils;

import java.util.ArrayList;
import java.util.List;

public class StringParser {

    private StringParser() {
    }

    /**
     * 将xml里的sql转化成preparedStatement预编译sql
     *
     * @param xmlSql 需要转化的sql
     * @return 转化完成后的sql
     */
    public static String parseSql(String xmlSql) {
        return xmlSql.replaceAll("#\\{[a-zA-Z0-9$_]*}", "?");
    }

    public static List<String> parseParameters(String xmlSql) {
        List<String> paramList = new ArrayList<>();
        int beginIndex = 0;
        while (true) {
            int left = xmlSql.indexOf("{", beginIndex);
            if (left < 0) {
                break;
            }
            int right = xmlSql.indexOf("}", left);
            //获取属性名
            String property = xmlSql.substring(left + 1, right).trim();
            beginIndex = right + 1;
            paramList.add(property);
        }
        return paramList;
    }

    /**
     * 将sql中的参数转化为Get方法
     *
     * @param xmlSql 需要解析的xmlsql语句
     * @return get方法名列表
     */
    public static List<String> toGetMethodName(String xmlSql) {
        List<String> paramList = parseParameters(xmlSql);
        return toGetMethodName(paramList);
    }

    /**
     * 根据参数转化成get方法
     *
     * @param paramList param参数列表
     * @return get方法名列表
     */
    public static List<String> toGetMethodName(List<String> paramList) {
        List<String> getMethodNameList = new ArrayList<>();

        paramList.forEach((param) -> {
            String getMethodName = "get" + param.toUpperCase().charAt(0) + param.substring(1);
            getMethodNameList.add(getMethodName);
        });

        return getMethodNameList;
    }

    /**
     * 将sql中的参数转化为Set方法
     *
     * @param xmlSql 需要解析的xmlsql语句
     * @return get方法名列表
     */
    public static List<String> toSetMethodName(String xmlSql) {
        List<String> paramList = parseParameters(xmlSql);
        return toSetMethodName(paramList);
    }


    /**
     * 根据参数转化成set方法
     *
     * @param paramList param参数列表
     * @return get方法名列表
     */
    public static List<String> toSetMethodName(List<String> paramList) {
        List<String> getMethodNameList = new ArrayList<>();

        paramList.forEach((param) -> {
            String getMethodName = "set" + param.toUpperCase().charAt(0) + param.substring(1);
            getMethodNameList.add(getMethodName);
        });

        return getMethodNameList;
    }
}
