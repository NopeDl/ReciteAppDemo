package pojo.po.db;

public class Umr {
    private int userId;
    private int modleId;
    private int  mStatus;

    //学习状态
    private String studyStatus;
    private String recordPath;

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

    public int getMStatus() {
        return mStatus;
    }

    public void setMStatus(int mStatus) {
        this.mStatus = mStatus;
    }


    public String getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(String studyStatus) {
        this.studyStatus = studyStatus;
    }


    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    @Override
    public String toString() {
        return "Umr{" +
                "userId=" + userId +
                ", modleId=" + modleId +
                ", mStatus=" + mStatus +
                ", studyStatus='" + studyStatus + '\'' +
                ", recordPath='" + recordPath + '\'' +
                '}';
    }
}
