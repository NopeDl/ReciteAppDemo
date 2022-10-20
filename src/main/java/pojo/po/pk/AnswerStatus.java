package pojo.po.pk;

/**
 * 封装答案信息
 * @author yeyeye
 * @Date 2022/10/21 1:25
 */
public class AnswerStatus {
    /**
     * 答案名字
     */
    private String answerName;
    /**
     * 该答案是否正确
     */
    private boolean isRight;

    public String getAnswerName() {
        return answerName;
    }

    public void setAnswerName(String answerName) {
        this.answerName = answerName;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public AnswerStatus() {
    }

    public AnswerStatus(String answerName, boolean isRight) {
        this.answerName = answerName;
        this.isRight = isRight;
    }
}
