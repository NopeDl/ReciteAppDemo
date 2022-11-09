package tools.easydao.utils;

import java.io.*;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Resources {

    private Resources() {
    }

    /**
     * 返回类路径下的资源
     *
     * @param path
     * @return
     */
    public static InputStream getResourceAsStream(String path) {
        String streamPath = Resources.class.getResource("/").getPath().substring(1) + path;
        FileInputStream input;
        try {
            input = new FileInputStream(streamPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public static String getResource(String path) {
        return Resources.class.getResource("/").getPath().substring(1) + path;
    }
}
