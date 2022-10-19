package tools.handlers;

import tools.handlers.impl.DocFileHandler;
import tools.handlers.impl.PDFFileHandler;
import tools.handlers.impl.TXTFileHandler;

import java.io.File;
import java.io.InputStream;

public class FileHandlerFactory {
    public static FileHandler getHandler(String fileType, InputStream input) {
        //根据文件类型获取解析器
        if ("pdf".equals(fileType)) {
            return new PDFFileHandler(input, FileType.PDF);
        } else if ("txt".equals(fileType)) {
            return new TXTFileHandler(input,FileType.TXT);
        } else if ("docx".equals(fileType)) {
            return new DocFileHandler(input,FileType.DOCX);
        } else if ("doc".equals(fileType)) {
            return new DocFileHandler(input,FileType.DOC);
        } else {
            return null;
        }
    }
}
