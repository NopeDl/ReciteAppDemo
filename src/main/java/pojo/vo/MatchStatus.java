package pojo.vo;

/**
 * @author yeyeye
 * @Date 2022/10/18 1:37
 */
public class MatchStatus {
    private int userId;//选择pk的用户的userId
    private int modleId;//要pk的modleId;
    private int modleNum;//目标模板的字数
    private int difficulty;//用户选择的难度，有三种，1是简单；2是中等；3是困难
    private String context;//获取模板的内容

    public MatchStatus() {
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

    public int getModleNum() {
        return modleNum;
    }

    public void setModleNum(int modleNum) {
        this.modleNum = modleNum;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "MatchInf{" +
                "userId=" + userId +
                ", modleId=" + modleId +
                ", modleNum=" + modleNum +
                ", context='" + context + '\'' +
                '}';
    }
}
