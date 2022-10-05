package pojo.po;

public class File {
    private int fileId;
    private String fileContext;
    private int userId;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileContext() {
        return fileContext;
    }

    public void setFileContext(String fileContext) {
        this.fileContext = fileContext;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public File() {
    }

    public File(int fileId, String fileContext, int userId) {
        this.fileId = fileId;
        this.fileContext = fileContext;
        this.userId = userId;
    }
}
