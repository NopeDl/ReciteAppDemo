package dao;

public interface LabelDao {

    /**
     * 根据标签的id获取标签的名字
     */
    String selectLableName(int labelId);
}
