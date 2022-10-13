package pojo.po;

public class Label {
    private int labelId;
    private String labelName;

    public Label() {
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int lableId) {
        this.labelId = labelId;
    }

    public String getLableName() {
        return labelName;
    }

    public void setLableIdName(String lableIdIdName) {
        this.labelName = labelName;
    }

    @Override
    public String toString() {
        return "Label{" +
                "labelId=" + labelId +
                ", labelName='" + labelName + '\'' +
                '}';
    }
}
