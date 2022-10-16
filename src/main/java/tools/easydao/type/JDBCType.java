package tools.easydao.type;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public enum JDBCType {
    INT(Types.INTEGER),
    BIGINT(Types.BIGINT),
    DOUBLE(Types.DOUBLE),
    FLOAT(Types.FLOAT),
    VARCHAR(Types.VARCHAR),
    CHAR(Types.CHAR),
    DATE(Types.DATE);

    /**
     * 获取sqlType值
     */
    public final int value;

    private static final Map<Class, JDBCType> jdbcTypeMapper;
    private static final Map<Integer, Class> javaTypeMapper;

    static {
        /**
         * 将JAVA类映射成JDBC类
         */
        jdbcTypeMapper = new HashMap<>();
        jdbcTypeMapper.put(String.class, VARCHAR);
        jdbcTypeMapper.put(char.class, CHAR);
        jdbcTypeMapper.put(Integer.class, INT);
        jdbcTypeMapper.put(int.class, INT);
        jdbcTypeMapper.put(Long.class, BIGINT);
        jdbcTypeMapper.put(long.class, BIGINT);
        jdbcTypeMapper.put(Double.class, DOUBLE);
        jdbcTypeMapper.put(double.class, DOUBLE);
        jdbcTypeMapper.put(Float.class, FLOAT);
        jdbcTypeMapper.put(float.class, FLOAT);
        jdbcTypeMapper.put(Date.class, DATE);

        /**
         * 将JDBC类映射成JAVA类
         */
        javaTypeMapper = new HashMap<>();
        javaTypeMapper.put(VARCHAR.value,String.class);
        javaTypeMapper.put(CHAR.value,String.class);
        javaTypeMapper.put(INT.value, int.class);
        javaTypeMapper.put(BIGINT.value,long.class);
        javaTypeMapper.put(DOUBLE.value,double.class);
        javaTypeMapper.put(FLOAT.value,float.class);
        javaTypeMapper.put(DATE.value,Date.class);
    }

    JDBCType(int value) {
        this.value = value;
    }

    public static JDBCType getJDBCType(String className) {
        return jdbcTypeMapper.get(className);
    }

    public static Class getJavaType(int value) {
        return javaTypeMapper.get(value);
    }
}
