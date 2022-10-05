package utils;

import java.io.*;

public class FileUtil {
    /**
     * 将文件保存至path
     *
     * @param path
     * @param input
     */
    public static void save(String path, InputStream input) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            //文件不存在创建一个
            file.createNewFile();
        }
        //创建output流
        OutputStream out = new FileOutputStream(file);
        //从input中读出并写入本地
        byte[] bytes = new byte[1024];
        int len;
        while ((len = input.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        //关闭流
        out.close();
    }
}
