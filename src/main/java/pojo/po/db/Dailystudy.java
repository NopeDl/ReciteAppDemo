package pojo.po.db;

import java.time.LocalDate;

public class Dailystudy {
    private int userId;
    private int studyNums;
    private int studyTime;
    private int reviewNums;
    private LocalDate storeTime;

    public Dailystudy() {
    }

    public Dailystudy(int userId, int studyNums, int studyTime, int reviewNums, LocalDate storeTime) {
        this.userId = userId;
        this.studyNums = studyNums;
        this.studyTime = studyTime;
        this.reviewNums = reviewNums;
        this.storeTime = storeTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStudyNums() {
        return studyNums;
    }

    public void setStudyNums(int studyNums) {
        this.studyNums = studyNums;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    public int getReviewNums() {
        return reviewNums;
    }

    public void setReviewNums(int reviewNums) {
        this.reviewNums = reviewNums;
    }

    public LocalDate getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(LocalDate storeTime) {
        this.storeTime = storeTime;
    }

    @Override
    public String toString() {
        return "Dailystudy{" +
                "userId=" + userId +
                ", studyNums=" + studyNums +
                ", studyTime=" + studyTime +
                ", reviewNums=" + reviewNums +
                ", storeTime=" + storeTime +
                '}';
    }
}
