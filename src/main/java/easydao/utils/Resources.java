package easydao.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Resources {

    private Resources() {
    }

    /**
     * ������·���µ���Դ
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
}
