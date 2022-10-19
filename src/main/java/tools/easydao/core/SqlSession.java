package tools.easydao.core;

import tools.easydao.pojo.SqlStatement;
import tools.easydao.type.JDBCType;
import tools.easydao.type.TypeHandler;
import tools.easydao.utils.StringParser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlSession {
    private SqlSessionFactory sqlSessionFactory;


    public SqlSession() {
    }

    public SqlSession(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 提交
     */
    public void commit() {
        sqlSessionFactory.getTransactionManager().commit();
    }

    /**
     * 关闭
     */
    public void close() {
        sqlSessionFactory.getTransactionManager().close();
    }

    /**
     * 连接
     */
    public void rollBack() {
        sqlSessionFactory.getTransactionManager().rollBack();
    }


    /**
     * 插入
     *
     * @param sqlId
     * @param pojo
     * @return
     */
    public int insert(String sqlId, Object pojo) {
        return normalDMLOperate(sqlId, pojo);
    }

    /**
     * 插入
     * @param sqlId
     * @return
     */
    public int insert(String sqlId) {
        String sql = sqlSessionFactory.getStatementMappers().get(sqlId).getSql();
        Connection connection = sqlSessionFactory.getTransactionManager().getConnection();
        int i = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            i = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("sql语句:" + sql + " 出错");
        }
        return i;
    }

    /**
     * 删除
     *
     * @param sqlId
     * @param pojo
     * @return
     */
    public int delete(String sqlId, Object pojo) {
        return normalDMLOperate(sqlId, pojo);
    }

    /**
     * 更新
     *
     * @param sqlId
     * @param pojo
     * @return
     */
    public int update(String sqlId, Object pojo) {
        return normalDMLOperate(sqlId, pojo);
    }

    /**
     * 通用增删改方法
     */
    private synchronized int normalDMLOperate(String sqlId, Object pojo) {
        //预编译ps
        PreparedStatement ps = setPreparedStatementParam(sqlId, pojo);
        //设置完PS
        int i = 0;
        try {
            if (ps != null) {
                i = ps.executeUpdate();
            }
        } catch (SQLException e) {
            //事务回滚
            rollBack();
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    /**
     * 查询
     *
     * @param sqlId sql语句ID
     * @param param pojo
     */
    public synchronized List<Object> selectList(String sqlId, Object param) {
        List<Object> resultList = new ArrayList<>();
        //预编译sql指令
        PreparedStatement ps = setPreparedStatementParam(sqlId, param);
        //获取全限定名
        SqlStatement sqlStatement = sqlSessionFactory.getStatementMappers().get(sqlId);
        String resultType = sqlStatement.getResultType();
        //获取xmlsql
        String xmlsql = sqlStatement.getSql();

        //执行查询获取结果集
        ResultSet rs = null;


        try {
            //获取resultType类属性名
            Class<?> clazz = Class.forName(resultType);
            Field[] fields = clazz.getFields();
            List<String> fieldsNameList = new ArrayList<>();
            for (Field field : fields) {
                fieldsNameList.add(field.getName());
            }
            //执行查询

            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {

                int columnCount = metaData.getColumnCount();
                //实例化resultType
                Object o = clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    //根据列名获取属性名
                    String columnName = metaData.getColumnLabel(i);
                    //获取类型处理器
                    int columnType = metaData.getColumnType(i);
                    Class javaType = JDBCType.getJavaType(columnType);

                    if (javaType != null) {
                         TypeHandler typeHandler = sqlSessionFactory.getTypeHandleRegistry().getHandler(javaType.getTypeName());
                        //根据属性名获取数据并封装成一个pojo
                        Object fieldVal = typeHandler.getResult(rs, columnName);
                        //根据属性名获取set方法
                        String methodName = "set" + columnName.toUpperCase().charAt(0) + columnName.substring(1);
                        //执行set方法
                        clazz.getDeclaredMethod(methodName,javaType).invoke(o,fieldVal);
                    } else {
                        throw new RuntimeException("javaType为空");
                    }
                }
                resultList.add(o);
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询错误");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(resultType + "类错误");
        } catch (InstantiationException e) {
            throw new RuntimeException(resultType + "类实例化错误");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(resultType + "类非法访问");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            //释放资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return resultList;
    }


    /**
     * 设置ps参数
     *
     * @param sqlId sql语句id
     * @param pojo  pojo
     * @return 设置完毕的preparedStatement
     */
    private synchronized PreparedStatement setPreparedStatementParam(String sqlId, Object pojo) {
        //获取连接
        Connection connection = sqlSessionFactory.getTransactionManager().getConnection();
        //创建PreparedStatement
        //获取xml sql
        String xmlSql = sqlSessionFactory.getStatementMappers().get(sqlId).getSql();
        //insert into user (username,password) values (#{username},#{password})
        //处理sql(将xmlSql参数转成 ? )
        String sql = StringParser.parseSql(xmlSql);
        //insert into user (username,password) values (?,?)
        //获得参数并设置参数
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        List<String> methodNameList = StringParser.toGetMethodName(xmlSql);

        try {
            int count = 1;
            for (String methodName : methodNameList) {
                //获取方法
                Method declaredMethod = pojo.getClass().getDeclaredMethod(methodName);
                //根据方法获得返回值全限定类名
                String className = declaredMethod.getGenericReturnType().getTypeName();
                //根据全限定类名获取处理器
                TypeHandler handler = getSqlSessionFactory().getTypeHandleRegistry().getHandler(className);
                //执行get方法获取pojo属性值
                Object param = declaredMethod.invoke(pojo);
                //执行处理器方法将pojo属性值填入对应preparedStatement中
                handler.setParameter(ps, count, param, JDBCType.getJDBCType(className));
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
    }
}
