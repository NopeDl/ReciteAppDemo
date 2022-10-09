package easydao.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseTypeHandler<T> implements TypeHandler<T> {
    @Override
    public void setParameter(PreparedStatement ps, int col, T param, JDBCType jdbcType) {
        if (param == null) {
            //如果参数为空,设置null值
            if (jdbcType != null) {
                try {
                    ps.setNull(col, jdbcType.value);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("jdbcType不能为空");
            }
        } else {
            //执行各类型对应的set非null值方法
            try {
                setNotNullParam(ps, col, param, jdbcType);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public T getResult(ResultSet rs, String col) {
        try {
            return getNullableResult(rs, col);
        } catch (SQLException e) {
            throw new RuntimeException("获取" + col + "错误");
        }
    }

    /**
     * 让子类重写的非null值设置方法
     *
     * @param ps       preparedStatement
     * @param col      需要设置的列下标
     * @param param    设置的参数
     * @param jdbcType 对应类型
     */
    protected abstract void setNotNullParam(PreparedStatement ps, int col, T param, JDBCType jdbcType) throws SQLException;

    protected abstract T getNullableResult(ResultSet rs, String col) throws SQLException;
}
