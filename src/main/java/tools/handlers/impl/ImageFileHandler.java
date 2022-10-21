package tools.handlers.impl;

import tools.handlers.BaseFileHandler;
import tools.handlers.FileType;

import java.io.*;
import java.nio.file.Files;

/**
 * @author yeyeye
 * @Date 2022/10/21 16:07
 */
public class ImageFileHandler extends BaseFileHandler {
    public ImageFileHandler(InputStream input, FileType fileType) {
        super(input, fileType);
    }

    @Override
    public String parseContent() {
        return this.parseContent(getInput());
    }

    /**
     * 获取文件路径
     * @param input 文件读取字节流
     * @return
     */
    @Override
    public String parseContent(InputStream input) {
        return null;
    }

    @Override
    public String saveFile(String filePath, String context) {
        try{
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            OutputStream out = Files.newOutputStream(file.toPath());
            InputStream input = getInput();
            int len;
            byte[] bytes = new byte[1024];
            while ((len = input.read(bytes))!= -1){
                out.write(bytes,0,len);
            }
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
