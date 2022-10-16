package tools.handlers.impl;

import tools.handlers.FileHandler;

import java.io.*;

public class TXTFileHandler implements FileHandler {
    private InputStream input;

    /**
     * 禁止访问无参构造
     */
    private TXTFileHandler() {
    }

    public TXTFileHandler(InputStream input) {
        this.input = input;
    }


    @Override
    public String parseContent() {
        return this.parseContent(input);
    }

    @Override
    public String parseContent(InputStream input) {
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
