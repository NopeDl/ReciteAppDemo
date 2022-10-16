package tools.easydao.type.impl;

import tools.easydao.type.BaseTypeHandler;
import tools.easydao.type.JDBCType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatTypeHandler extends BaseTypeHandler<Float> {
    @Override
    protected void setNotNullParam(PreparedStatement ps, int col, Float param, JDBCType jdbcType) throws SQLException {
        ps.setFloat(col, param);
    }

    @Override
    protected Float getNullableResult(ResultSet rs, String col) throws SQLException {
        float db = rs.getFloat(col);
        return db == 0 && rs.wasNull() ? null : db;
    }
}
