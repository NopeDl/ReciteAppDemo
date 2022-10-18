package tools.handlers;

import java.io.InputStream;

/**
 * @author yeyeye
 * @Date 2022/10/19 1:46
 */
public abstract class BaseFileHandler implements FileHandler {
    private InputStream input;

    private FileType fileType;

    private BaseFileHandler(){}

    public BaseFileHandler(InputStream input,FileType fileType) {
        this.input = input;
        this.fileType = fileType;
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
