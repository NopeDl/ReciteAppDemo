package pojo.po.db;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Review {
    private int modleId;
    private LocalDate reTime;//进入复习的时间
    private int period;//复习周期


    public Review() {
    }


    public Review(int modleId, LocalDate reTime, int period) {
        this.modleId = modleId;
        this.reTime = reTime;
        this.period = period;
    }

    public int getModleId() {
        return modleId;
    }

    public void setModleId(int modleId) {
        this.modleId = modleId;
    }

    public LocalDate getReTime() {
        return reTime;
    }

    public void setReTime(LocalDate reTime) {
        this.reTime = reTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "Review{" +
                "modleId=" + modleId +
                ", reTime=" + reTime +
                ", period=" + period +
                '}';
    }
}
