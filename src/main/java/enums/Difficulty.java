package enums;

import com.sun.javafx.collections.MappingChange;
import org.apache.xmlbeans.impl.util.Diff;

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
    EASY(25),
    NORMAL(45),
    HARD(50);

    private int ratio;
    private Map<String,Difficulty> map = new HashMap<>();

    Difficulty(int ratio) {
        this.ratio = ratio;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }


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
