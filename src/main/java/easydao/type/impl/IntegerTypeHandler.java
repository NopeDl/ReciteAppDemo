package easydao.type.impl;

import easydao.type.BaseTypeHandler;
import easydao.type.JDBCType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerTypeHandler extends BaseTypeHandler<Integer> {
    @Override
    protected void setNotNullParam(PreparedStatement ps, int col, Integer param, JDBCType jdbcType)
            throws SQLException {
        ps.setInt(col, param);
    }

    @Override
    protected Integer getNullableResult(ResultSet rs, String col) throws SQLException {
        int ret = rs.getInt(col);
        return ret == 0 && rs.wasNull() ? null : ret;
    }
}
