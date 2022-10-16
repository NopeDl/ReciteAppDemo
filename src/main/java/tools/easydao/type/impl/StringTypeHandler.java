package tools.easydao.type.impl;

import tools.easydao.type.BaseTypeHandler;

import tools.easydao.type.JDBCType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler extends BaseTypeHandler<String> {
    @Override
    protected void setNotNullParam(PreparedStatement ps, int col, String param, JDBCType jdbcType)
            throws SQLException {
        ps.setString(col, param);
    }

    @Override
    protected String getNullableResult(ResultSet rs, String col) throws SQLException {
        return rs.getString(col);
    }
}
