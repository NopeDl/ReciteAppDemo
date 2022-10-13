package pojo.po;

public class Umr {
    private int userId;
    private int modleId;
    private int  mStatus;

    public Umr() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getModleId() {
        return modleId;
    }

    public void setModleId(int modleId) {
        this.modleId = modleId;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }


    @Override
    public String toString() {
        return "Umr{" +
                "userId=" + userId +
                ", modleId=" + modleId +
                ", mStatus=" + mStatus +
                '}';
    }
}
