package enums;

import pojo.po.db.Review;

public enum ReviewPeriod {


    /**
     * 周期和对应的天数
     */
    ONE(1,1),
    SECOND(2,2),
    THIRD(3,4),
    FOURTH(4,7),
    FIFTH(5,15),
    SIXTH(6,31),
    SEVENTH(7,90),
    EIGHTH(8,180);


    //存储周期，一共八个周期
    private int period;

    //存储周期变化所需要的时间
    private int date;

    ReviewPeriod() {
    }

    ReviewPeriod(int period, int date) {
        this.period = period;
        this.date = date;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public static ReviewPeriod getReviewPeriod(int period){
        switch (period){
            case 1:return ReviewPeriod.ONE;
            case 2:return ReviewPeriod.SECOND;
            case 3:return ReviewPeriod.THIRD;
            case 4:return ReviewPeriod.FOURTH;
            case 5:return ReviewPeriod.FIFTH;
            case 6:return ReviewPeriod.SIXTH;
            case 7:return ReviewPeriod.SEVENTH;
            case 8:return ReviewPeriod.EIGHTH;
        }
        return null;
    }
}
