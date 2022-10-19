package pojo.vo;

import java.util.Objects;

/**
 * 封装匹配用户的相关信息
 * @author h2012
 */
public class MatchInf {
    /**
     * 匹配用户的id
     */
    private int userId;

    /**
     * 用户参赛模板id
     */
    private int modleId;

    /**
     * 模板字数
     */
    private int modleNum;

    /**
     * 难度
     */
    private String difficulty;

    /**
     * 模板内容
     */
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MatchInf matchInf = (MatchInf) o;
        return userId == matchInf.userId && modleId == matchInf.modleId && modleNum == matchInf.modleNum && Objects.equals(difficulty, matchInf.difficulty) && Objects.equals(content, matchInf.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, modleId, modleNum, difficulty, content);
    }

    public MatchInf() {
    }


    public int getModleNum() {
        return modleNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setModleNum(int modleNum) {
        this.modleNum = modleNum;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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
