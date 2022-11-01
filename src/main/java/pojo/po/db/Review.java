package pojo.po.db;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author isDucka
 */
public class Review {
    private int modleId;
    //进入复习的时间
    private LocalDate reTime;
    //复习周期
    private int period;
    //显示周期所需要的天数
    private int days;
    //用户id
    private int userId;


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

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Review{" +
                "modleId=" + modleId +
                ", reTime=" + reTime +
                ", period=" + period +
                ", days=" + days +
                ", userId=" + userId +
                '}';
    }
}
