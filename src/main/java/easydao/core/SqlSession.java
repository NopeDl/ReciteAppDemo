package easydao.core;

import easydao.pojo.SqlStatement;
import easydao.type.JDBCType;
import easydao.type.TypeHandler;
import easydao.utils.StringParser;

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
     * �ύ
     */
    public void commit() {
        sqlSessionFactory.getTransactionManager().commit();
    }

    /**
     * �ر�
     */
    public void close() {
        sqlSessionFactory.getTransactionManager().close();
    }

    /**
     * ����
     */
    public void rollBack() {
        sqlSessionFactory.getTransactionManager().rollBack();
    }


    /**
     * ����
     *
     * @param sqlId
     * @param pojo
     * @return
     */
    public int insert(String sqlId, Object pojo) {
        return normalDMLOperate(sqlId, pojo);
    }

    /**
     * ����
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
            throw new RuntimeException("sql���:" + sql + " ����");
        }
        return i;
    }

    /**
     * ɾ��
     *
     * @param sqlId
     * @param pojo
     * @return
     */
    public int delete(String sqlId, Object pojo) {
        return normalDMLOperate(sqlId, pojo);
    }

    /**
     * ����
     *
     * @param sqlId
     * @param pojo
     * @return
     */
    public int update(String sqlId, Object pojo) {
        return normalDMLOperate(sqlId, pojo);
    }

    /**
     * ͨ����ɾ�ķ���
     */
    private int normalDMLOperate(String sqlId, Object pojo) {
        //Ԥ����ps
        PreparedStatement ps = setPreparedStatementParam(sqlId, pojo);
        //������PS
        int i = 0;
        try {
            if (ps != null) {
                i = ps.executeUpdate();
            }
        } catch (SQLException e) {
            //����ع�
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
     * ��ѯ
     *
     * @param sqlId sql���ID
     * @param param pojo
     */
    public List<Object> selectList(String sqlId, Object param) {
        List<Object> resultList = new ArrayList<>();
        //Ԥ����sqlָ��
        PreparedStatement ps = setPreparedStatementParam(sqlId, param);
        //��ȡȫ�޶���
        SqlStatement sqlStatement = sqlSessionFactory.getStatementMappers().get(sqlId);
        String resultType = sqlStatement.getResultType();
        //��ȡxmlsql
        String xmlsql = sqlStatement.getSql();

        //ִ�в�ѯ��ȡ�����
        ResultSet rs = null;


        try {
            //��ȡresultType��������
            Class<?> clazz = Class.forName(resultType);
            Field[] fields = clazz.getFields();
            List<String> fieldsNameList = new ArrayList<>();
            for (Field field : fields) {
                fieldsNameList.add(field.getName());
            }
            //ִ�в�ѯ

            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {

                int columnCount = metaData.getColumnCount();
                //ʵ����resultType
                Object o = clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    //��ȡ������
                    String columnName = metaData.getColumnLabel(i);
                    //��ȡ���ʹ�����
                    int columnType = metaData.getColumnType(i);
                    System.out.println();
                    Class javaType = JDBCType.getJavaType(columnType);

                    if (javaType != null) {
                         TypeHandler typeHandler = sqlSessionFactory.getTypeHandleRegistry().getHandler(javaType.getTypeName());
                        //������������ȡ���ݲ���װ��һ��pojo
                        Object fieldVal = typeHandler.getResult(rs, columnName);
                        //������������ȡset����
                        String methodName = "set" + columnName.toUpperCase().charAt(0) + columnName.substring(1);
                        //ִ��set����
                        clazz.getDeclaredMethod(methodName,javaType).invoke(o,fieldVal);
                    } else {
                        throw new RuntimeException("javaTypeΪ��");
                    }
                }
                resultList.add(o);
            }
        } catch (SQLException e) {
            throw new RuntimeException("��ѯ����");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(resultType + "�����");
        } catch (InstantiationException e) {
            throw new RuntimeException(resultType + "��ʵ��������");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(resultType + "��Ƿ�����");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            //�ͷ���Դ
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
     * ����ps����
     *
     * @param sqlId sql���id
     * @param pojo  pojo
     * @return ������ϵ�preparedStatement
     */
    private PreparedStatement setPreparedStatementParam(String sqlId, Object pojo) {
        //��ȡ����
        Connection connection = sqlSessionFactory.getTransactionManager().getConnection();
        //����PreparedStatement
        //��ȡxml sql
        String xmlSql = sqlSessionFactory.getStatementMappers().get(sqlId).getSql();
        //insert into user (username,password) values (#{username},#{password})
        //����sql(��xmlSql����ת�� ? )
        String sql = StringParser.parseSql(xmlSql);
        //insert into user (username,password) values (?,?)
//        ��ò��������ò���
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
                //��ȡ����
                Method declaredMethod = pojo.getClass().getDeclaredMethod(methodName);
                //���ݷ�����÷���ֵȫ�޶�����
                String className = declaredMethod.getGenericReturnType().getTypeName();
                //����ȫ�޶�������ȡ������
                TypeHandler handler = getSqlSessionFactory().getTypeHandleRegistry().getHandler(className);
                //ִ��get������ȡpojo����ֵ
                Object param = declaredMethod.invoke(pojo);
                //ִ�д�����������pojo����ֵ�����ӦpreparedStatement��
                handler.setParameter(ps, count, param, JDBCType.getJDBCType(className));
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
    }
}
