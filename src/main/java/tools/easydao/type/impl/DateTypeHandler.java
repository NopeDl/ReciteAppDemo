package tools.easydao.type.impl;

import tools.easydao.type.BaseTypeHandler;

import tools.easydao.type.JDBCType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DateTypeHandler extends BaseTypeHandler<Date> {

    @Override
    protected void setNotNullParam(PreparedStatement ps, int col, Date param, JDBCType jdbcType)
            throws SQLException {

        java.sql.Date sqlDate = new java.sql.Date(param.getTime());
        ps.setDate(col, sqlDate);
    }

    @Override
    protected Date getNullableResult(ResultSet rs, String col) throws SQLException {
        java.sql.Date date = rs.getDate(col);
        Date jdate = new Date(date.getTime());
        return jdate;
    }
}
