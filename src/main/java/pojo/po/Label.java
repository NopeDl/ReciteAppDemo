package pojo.po;

public class Label {
    private int labelId;
    private String labelName;

    public Label() {
    }

    public Label(int labelId, String labelName) {
        this.labelId = labelId;
        this.labelName = labelName;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
