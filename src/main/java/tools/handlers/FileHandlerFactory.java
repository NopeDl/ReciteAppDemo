package tools.handlers;

import tools.handlers.impl.PDFFileHandler;
import tools.handlers.impl.TXTFileHandler;

import java.io.InputStream;

public class FileHandlerFactory {
    public FileHandler getHandler(String fileType, InputStream input) {
        //根据文件类型获取解析器
        if (fileType.contains("pdf")) {
            return new PDFFileHandler(input);
        } else if (fileType.contains("txt")) {
            return new TXTFileHandler(input);
        } else {
            return null;
        }
    }
}
