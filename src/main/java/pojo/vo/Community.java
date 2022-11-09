package pojo.vo;

import java.time.LocalDate;

/**
 * 返回模板社区的内容
 */
public class Community {
    private int modleId;
    private String modleTitle;
    private String modlePath;
    private int modleLabel;
    /**
     * 模板内容
     */
    private String content;
    private int coins;
    private int userId;
    private String nickName;
    private String base64;

    private int common;

    private LocalDate createTime;


    private String studyStatus;

    //判断用户是否点赞过改条帖子
    private boolean likeStatus;

    //该帖子获赞数量
    private int likeNum;


    public Community() {
    }


    public int getModleId() {
        return modleId;
    }

    public void setModleId(int modleId) {
        this.modleId = modleId;
    }

    public String getModleTitle() {
        return modleTitle;
    }

    public void setModleTitle(String modleTitle) {
        this.modleTitle = modleTitle;
    }

    public String getModlePath() {
        return modlePath;
    }

    public void setModlePath(String modlePath) {
        this.modlePath = modlePath;
    }

    public int getModleLabel() {
        return modleLabel;
    }

    public void setModleLabel(int modleLabel) {
        this.modleLabel = modleLabel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    //    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }


    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }


    public int getCommon() {
        return common;
    }

    public void setCommon(int common) {
        this.common = common;
    }

    public String getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(String studyStatus) {
        this.studyStatus = studyStatus;
    }


    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    @Override
    public String toString() {
        return "Community{" +
                "modleId=" + modleId +
                ", modleTitle='" + modleTitle + '\'' +
                ", modlePath='" + modlePath + '\'' +
                ", modleLabel=" + modleLabel +
                ", content='" + content + '\'' +
                ", coins=" + coins +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", base64='" + base64 + '\'' +
                ", common=" + common +
                ", createTime=" + createTime +
                ", studyStatus='" + studyStatus + '\'' +
                ", likeStatus=" + likeStatus +
                ", likeNum=" + likeNum +
                '}';
    }
}