package pojo.vo;

/**
 * 封装匹配用户的相关信息
 */
public class MatchStatus {
    /**
     * 匹配用户的id
     */
    private int userId;

    /**
     * 用户参赛模板id
     */
    private int modleId;

    public MatchStatus() {
    }

    public MatchStatus(int userId, int modleId) {
        this.userId = userId;
        this.modleId = modleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getModleId() {
        return modleId;
    }

    public void setModleId(int modleId) {
        this.modleId = modleId;
    }
}
