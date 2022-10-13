package pojo.po;

public class Modle {
    private int modleId;
    private String modleTitle;
    private String modlePath;
    private int userId;
    private int modleLabel;


    private int coins;

    /**
     * sql语句中limit使用
     */
    private int pageIndex;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Modle() {
    }



    public Modle(int modleId, String modleTitle, String modlePath, int userId, int modleLabel) {
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


    public int getModleLabel() {
        return modleLabel;
    }

    public void setModleLable(int modleLable) {
        this.modleLabel = modleLabel;
    }


    @Override
    public String toString() {
        return "Modle{" +
                "modleId=" + modleId +
                ", modleTitle='" + modleTitle + '\'' +
                ", modlePath='" + modlePath + '\'' +
                ", userId=" + userId +
                ", modleLable=" + modleLabel +
                ", coins=" + coins +
                ", pageIndex=" + pageIndex +
                '}';
    }
}
