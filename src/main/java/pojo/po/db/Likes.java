package pojo.po.db;

public class Likes {
    //执行点赞的用户
    private int userId;
    //被点赞的模板
    private int modleId;

    public Likes() {
    }

    public Likes(int userId, int modleId) {
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
    public String toString() {
        return "Like{" +
                "userId=" + userId +
                ", modleId=" + modleId +
                '}';
    }
}
