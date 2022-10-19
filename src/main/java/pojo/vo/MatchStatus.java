package pojo.vo;

import java.util.Objects;

/**
 * 封装匹配用户的相关信息
 * @author h2012
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

    private int wordsNum;

    public int getWordsNum() {
        return wordsNum;
    }

    public void setWordsNum(int wordsNum) {
        this.wordsNum = wordsNum;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MatchStatus that = (MatchStatus) o;
        return userId == that.userId && modleId == that.modleId && wordsNum == that.wordsNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, modleId, wordsNum);
    }
}
