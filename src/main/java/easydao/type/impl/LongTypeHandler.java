package easydao.type.impl;

import easydao.type.BaseTypeHandler;
import easydao.type.JDBCType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler extends BaseTypeHandler<Long> {
    @Override
    protected void setNotNullParam(PreparedStatement ps, int col, Long param, JDBCType jdbcType) throws SQLException {
        ps.setLong(col, param);

    }

    @Override
    protected Long getNullableResult(ResultSet rs, String col) throws SQLException {
        long ret = rs.getLong(col);
        return ret == 0 && rs.wasNull() ? null : ret;
    }
}
