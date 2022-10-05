package easydao.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseTypeHandler<T> implements TypeHandler<T> {
    @Override
    public void setParameter(PreparedStatement ps, int col, T param, JDBCType jdbcType) {
        if (param == null) {
            //�������Ϊ��,����nullֵ
            if (jdbcType != null) {
                try {
                    ps.setNull(col, jdbcType.value);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("jdbcType����Ϊ��");
            }
        } else {
            //ִ�и����Ͷ�Ӧ��set��nullֵ����
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
            throw new RuntimeException("��ȡ" + col + "����");
        }
    }

    /**
     * ��������д�ķ�nullֵ���÷���
     *
     * @param ps       preparedStatement
     * @param col      ��Ҫ���õ����±�
     * @param param    ���õĲ���
     * @param jdbcType ��Ӧ����
     */
    protected abstract void setNotNullParam(PreparedStatement ps, int col, T param, JDBCType jdbcType) throws SQLException;

    protected abstract T getNullableResult(ResultSet rs, String col) throws SQLException;
}
