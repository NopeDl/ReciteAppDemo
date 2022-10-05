package easydao.pojo;

public class SqlStatement {
    private String resultType;

    private String sql;


    public SqlStatement(String resultType, String sql) {
        this.resultType = resultType;
        this.sql = sql;
    }

    public SqlStatement() {
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
