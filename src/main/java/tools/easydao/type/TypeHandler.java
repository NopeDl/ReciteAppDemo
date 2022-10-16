package tools.easydao.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface TypeHandler<T> {
    void setParameter(PreparedStatement ps, int col, T param, JDBCType jdbcType);

    T getResult(ResultSet rs, String col);

}
