package easydao.type;

import easydao.type.impl.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeHandleRegistry {
    private final Map<String, TypeHandler<?>> jdbcTypeHandlerMapper;

    public TypeHandleRegistry() {
        jdbcTypeHandlerMapper = new HashMap<>();
        initTypeMapper();
    }

    /**
     * 注册java类与sql类型处理的关系
     */
    private void initTypeMapper() {
        jdbcTypeHandlerMapper.put(String.class.getTypeName(), new StringTypeHandler());
        jdbcTypeHandlerMapper.put(char.class.getTypeName(), new StringTypeHandler());

        jdbcTypeHandlerMapper.put(Integer.class.getTypeName(), new IntegerTypeHandler());
        jdbcTypeHandlerMapper.put(int.class.getTypeName(), new IntegerTypeHandler());

        jdbcTypeHandlerMapper.put(Long.class.getTypeName(), new LongTypeHandler());
        jdbcTypeHandlerMapper.put(long.class.getTypeName(), new LongTypeHandler());

        jdbcTypeHandlerMapper.put(Double.class.getTypeName(), new DoubleTypeHandler());
        jdbcTypeHandlerMapper.put(double.class.getTypeName(), new DoubleTypeHandler());

        jdbcTypeHandlerMapper.put(Float.class.getTypeName(), new FloatTypeHandler());
        jdbcTypeHandlerMapper.put(float.class.getTypeName(), new FloatTypeHandler());

        jdbcTypeHandlerMapper.put(Date.class.getTypeName(), new DateTypeHandler());
    }

    public TypeHandler getHandler(String className){
        return jdbcTypeHandlerMapper.get(className);
    }


}
