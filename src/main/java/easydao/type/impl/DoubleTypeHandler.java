package easydao.type.impl;

import easydao.type.BaseTypeHandler;
import easydao.type.JDBCType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleTypeHandler extends BaseTypeHandler<Double> {
    @Override
    protected void setNotNullParam(PreparedStatement ps, int col, Double param, JDBCType jdbcType) throws SQLException {
        ps.setDouble(col, param);
    }

    @Override
    protected Double getNullableResult(ResultSet rs, String col) throws SQLException {
        double db = rs.getDouble(col);
        return db == 0 && rs.wasNull() ? null : db;
    }
}
