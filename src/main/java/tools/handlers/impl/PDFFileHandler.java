package tools.handlers.impl;

import tools.handlers.FileHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

public class PDFFileHandler implements FileHandler {
    private InputStream input;

    /**
     * 禁止访问无参构造
     */
    private PDFFileHandler() {
    }

    public PDFFileHandler(InputStream input) {
        this.input = input;
    }

    public String parseContent() {
        return this.parseContent(input);
    }

    /**
     * 解析内容
     *
     * @param input 文件读取字节流
     * @return 解析的内容
     */
    @Override
    public String parseContent(InputStream input) {
        String context = null;
        PDDocument document = null;
        try {
            //读取流
            document = PDDocument.load(input);
            //解析文档
            PDFTextStripper stripper = new PDFTextStripper();
            //获取文档
            context = stripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return context;
    }

    @Override
    public String saveFile(String filePath, String context) {
        return null;
    }
}
