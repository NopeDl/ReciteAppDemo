package pojo.po.pk;

/**
 * 封装用户id和用户血量
 * @author yeyeye
 * @Date 2022/10/21 1:13
 */
public class UserHp {
    /**
     * 实际上为Token
     */
    private String userId;
    private double hp;


    public UserHp() {
    }

    public UserHp(String userId, double hp) {
        this.userId = userId;
        this.hp = hp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }
}
