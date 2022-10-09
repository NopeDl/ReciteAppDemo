package pojo.po;

public class Modle {
    private int modleId;
    private String modleTitle;
    private String modlePath;
    private int userId;

    public Modle() {
    }

    public Modle(int modleId, String modleTitle, String modlePath, int userId) {
        this.modleId = modleId;
        this.modleTitle = modleTitle;
        this.modlePath = modlePath;
        this.userId = userId;
    }

    public int getModleId() {
        return modleId;
    }

    public void setModleId(int modleId) {
        this.modleId = modleId;
    }

    public String getModleTitle() {
        return modleTitle;
    }

    public void setModleTitle(String modleTitle) {
        this.modleTitle = modleTitle;
    }

    public String getModlePath() {
        return modlePath;
    }

    public void setModlePath(String modlePath) {
        this.modlePath = modlePath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Modle{" +
                "modleId=" + modleId +
                ", modleTitle='" + modleTitle + '\'' +
                ", modlePath='" + modlePath + '\'' +
                ", userId=" + userId +
                '}';
    }
}
