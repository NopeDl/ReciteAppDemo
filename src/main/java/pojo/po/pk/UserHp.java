package pojo.po.pk;

/**
 * 封装用户id和用户血量
 * @author yeyeye
 * @Date 2022/10/21 1:13
 */
public class UserHp {
    private int userId;
    private double hp;

    public UserHp() {
    }

    public UserHp(int userId, double hp) {
        this.userId = userId;
        this.hp = hp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }
}
