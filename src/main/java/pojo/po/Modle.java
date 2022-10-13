package pojo.po;

public class Modle {
    private int modleId;
    private String modleTitle;
    private String modlePath;
    private int userId;

    private int modleLabel;

    private int coins;

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Modle() {
    }

    public int getModleLabel() {
        return modleLabel;
    }

    public void setModleLabel(int modleLabel) {
        this.modleLabel = modleLabel;
    }

    public Modle(int modleId, String modleTitle, String modlePath, int userId, int modleLabel) {
        this.modleId = modleId;
        this.modleTitle = modleTitle;
        this.modlePath = modlePath;
        this.userId = userId;
        this.modleLabel = modleLabel;
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
