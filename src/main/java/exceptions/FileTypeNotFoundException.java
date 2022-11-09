package exceptions;

/**
 * @author yeyeye
 * @Date 2022/11/9 15:12
 */
public class FileTypeNotFoundException extends Exception{
    public FileTypeNotFoundException() {
        super("文件类型错误");
    }
}
