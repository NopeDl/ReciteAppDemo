package tools.handlers.impl;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import tools.handlers.BaseFileHandler;
import tools.handlers.FileType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author yeyeye
 * @Date 2022/10/19 1:46
 */
public class DocFileHandler extends BaseFileHandler {
    public DocFileHandler(InputStream input,FileType fileType) {
        super(input,fileType);
    }

    @Override
    public String parseContent() {
        FileType fileType = getFileType();
        String content;
        if (FileType.DOCX.equals(fileType)) {
            content = docxParser();
        } else {
            content = docParser();
        }
        return content;
    }

    /**
     * 解析docx后缀文件名
     *
     * @return 解析的内容
     */
    private String docxParser() {
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(getInput());
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph paragraph : paragraphs) {
                sb.append("<p>");
                sb.append(paragraph.getParagraphText());
                sb.append("</p>");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析doc后缀文件名
     *
     * @return 解析的内容
     */
    private String docParser() {
        WordExtractor extractor = null;
        try {
            extractor = new WordExtractor(getInput());
            String[] paragraphs = extractor.getParagraphText();
            StringBuilder sb = new StringBuilder();
            for (String paragraph : paragraphs) {
                sb.append("<p>");
                sb.append(paragraph);
                sb.append("</p>");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (extractor != null) {
                try {
                    extractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String parseContent(InputStream input) {
        return null;
    }

    @Override
    public String saveFile(String filePath, String context) {
        return null;
    }
}
