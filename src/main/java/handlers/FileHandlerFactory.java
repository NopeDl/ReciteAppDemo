package handlers;

import handlers.impl.PDFFileHandler;

import java.io.InputStream;

public class FileHandlerFactory {
    public FileHandler getHandler(String fileType, InputStream input){
        //根据文件类型获取解析器
        if (fileType.contains("pdf")){
            return new PDFFileHandler(input);
        }else {
            return null;
        }
    }
}
