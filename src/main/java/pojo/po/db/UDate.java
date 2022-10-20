package pojo.po.db;

import java.time.LocalDate;
import java.util.Date;

public class UDate {
    private LocalDate date;

    private int userId;

    private String year;

    private String month;

    /**
     * 模糊查询表达式
     */
    private String exp;


    private String getYear() {
        return year;
    }


    public void setYear(String year) {
        this.year = year;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
