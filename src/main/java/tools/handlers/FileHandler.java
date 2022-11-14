package tools.handlers;

import java.io.IOException;
import java.io.InputStream;

public interface FileHandler {
    /**
     * 无参解析内容，自动调用有参的
     * @return 解析好的内容
     */
    String parseContent();

    /**
     * 解析内容
     *
     * @param input 文件读取字节流
     * @return 解析好的内容
     */
    String parseContent(InputStream input);

    /**
     * 保存文件
     * @return
     */
    String saveFile(String filePath, String context);
}
