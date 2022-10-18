package tools.easydao.type.impl;

import tools.easydao.type.BaseTypeHandler;
import tools.easydao.type.JDBCType;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;


/**
 * @author yeyeye
 * @Date 2022/10/19 0:35
 */
public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {
    @Override
    protected void setNotNullParam(PreparedStatement ps, int col, LocalDate param, JDBCType jdbcType) throws SQLException {
        Date date = Date.valueOf(param);
        ps.setDate(col,date);
    }

    @Override
    protected LocalDate getNullableResult(ResultSet rs, String col) throws SQLException {
        Date date = rs.getDate(col);
        return date.toLocalDate();
    }
}
