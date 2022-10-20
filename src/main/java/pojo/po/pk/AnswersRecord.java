package pojo.po.pk;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录用户比赛记录
 * @author yeyeye
 * @Date 2022/10/21 1:37
 */
public class AnswersRecord {
    private int userId;

    private List<AnswerStatus> answersRecord = new ArrayList<>();

    public AnswersRecord() {
    }

    public AnswersRecord(int userId, List<AnswerStatus> answersRecord) {
        this.userId = userId;
        this.answersRecord = answersRecord;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<AnswerStatus> getAnswersRecord() {
        return answersRecord;
    }

    public void setAnswersRecord(List<AnswerStatus> answersRecord) {
        this.answersRecord = answersRecord;
    }
}
