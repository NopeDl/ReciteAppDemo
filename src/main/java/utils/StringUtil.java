package utils;

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
}
