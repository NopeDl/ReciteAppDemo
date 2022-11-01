package pojo.po.db;

import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author isDucka
 */
public class Review {
    private int modleId;

    /**
     * 进入复习的时间
     */
    private LocalDate reTime;
    /**
     * 复习周期
     */
    private int period;


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
