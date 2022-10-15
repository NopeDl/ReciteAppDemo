package pojo.po;

public class Count {
    private Long number;

    public Long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setNumber(double number) {
        this.number = (long)number;
    }

    public Count() {
    }

    public Count(Long number) {
        this.number = number;
    }
}
