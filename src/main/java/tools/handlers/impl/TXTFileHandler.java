package tools.handlers.impl;

import tools.handlers.BaseFileHandler;
import tools.handlers.FileHandler;
import tools.handlers.FileType;

import java.io.*;

public class TXTFileHandler extends BaseFileHandler {
    public TXTFileHandler(InputStream input, FileType fileType) {
        super(input, fileType);
    }

    @Override
    public String parseContent(){
        return this.parseContent(getInput());
    }

    @Override
    public String parseContent(InputStream input){
        BufferedReader br = null;
        StringBuilder sb = null;
        String temp = "";
        try {
            br = new BufferedReader(new InputStreamReader(input));
            sb = new StringBuilder();
            while ((temp = br.readLine()) != null) {
                // 拼接换行符
                sb.append(temp + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String saveFile(String filePath, String context) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] bytes;
            bytes = context.getBytes();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }
}
