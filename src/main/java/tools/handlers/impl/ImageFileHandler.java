package tools.handlers.impl;

import sun.misc.BASE64Encoder;
import tools.handlers.BaseFileHandler;
import tools.handlers.FileType;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

import static javax.xml.crypto.dsig.Transform.BASE64;

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
        //将图片转成base64
        byte[] bytes = null;
        try {
            bytes = new byte[input.available()];
            input.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(bytes));
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
            byte[] bytes = new byte[input.available()];
            input.read(bytes);
            out.write(bytes);
            out.close();
//            int len;
//            byte[] bytes = new byte[1024];
//            while ((len = input.read(bytes))!= -1){
//                out.write(bytes,0,len);
//            }
//            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return filePath.substring(filePath.indexOf("ReciteMemory"));
    }
}
