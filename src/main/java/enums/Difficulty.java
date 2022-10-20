package enums;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yeyeye
 * @Date 2022/10/20 14:06
 */
public enum Difficulty {
    /**
     * 难度
     */
    EASY(25,10),
    NORMAL(45,15),
    HARD(50,20);

    /**
     * 难度比例
     */
    private int ratio;
    /**
     * 该难度下每个空所需的限制时间
     */
    private int timeLimits;

    private Map<String,Difficulty> map = new HashMap<>();

    Difficulty(int ratio,int timeLimits) {
        this.ratio = ratio;
        this.timeLimits = timeLimits;
    }

    public double getRatio() {
        return ratio / 100.0;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getTimeLimits() {
        return timeLimits;
    }

    public void setTimeLimits(int timeLimits) {
        this.timeLimits = timeLimits;
    }

    public Map<String, Difficulty> getMap() {
        return map;
    }

    public void setMap(Map<String, Difficulty> map) {
        this.map = map;
    }

    /**
     * 获取难度比例
     * @param difficult 难度描述
     * @return 返回难度枚举类
     */
    public static Difficulty getRatio(String difficult){
        if ("easy".equals(difficult)){
            return Difficulty.EASY;
        } else if ("normal".equals(difficult)) {
            return Difficulty.NORMAL;
        }else {
            return Difficulty.HARD;
        }
    }


}
