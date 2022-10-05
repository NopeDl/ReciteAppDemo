package easydao.utils;

import java.util.ArrayList;
import java.util.List;

public class StringParser {

    private StringParser() {
    }

    /**
     * ��xml���sqlת����preparedStatementԤ����sql
     *
     * @param xmlSql ��Ҫת����sql
     * @return ת����ɺ��sql
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
            //��ȡ������
            String property = xmlSql.substring(left + 1, right).trim();
            beginIndex = right + 1;
            paramList.add(property);
        }
        return paramList;
    }

    /**
     * ��sql�еĲ���ת��ΪGet����
     *
     * @param xmlSql ��Ҫ������xmlsql���
     * @return get�������б�
     */
    public static List<String> toGetMethodName(String xmlSql) {
        List<String> paramList = parseParameters(xmlSql);
        return toGetMethodName(paramList);
    }

    /**
     * ���ݲ���ת����get����
     *
     * @param paramList param�����б�
     * @return get�������б�
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
     * ��sql�еĲ���ת��ΪSet����
     *
     * @param xmlSql ��Ҫ������xmlsql���
     * @return get�������б�
     */
    public static List<String> toSetMethodName(String xmlSql) {
        List<String> paramList = parseParameters(xmlSql);
        return toSetMethodName(paramList);
    }


    /**
     * ���ݲ���ת����set����
     *
     * @param paramList param�����б�
     * @return get�������б�
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
