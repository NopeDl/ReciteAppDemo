package tools.utils;

import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import tools.easydao.core.SqlSessionFactoryBuilder;
import tools.easydao.utils.Resources;

public class DaoUtil {
    private static final SqlSessionFactory SQL_SESSION_FACTORY;

    private static final ThreadLocal<SqlSession> THREAD_LOCAL = new ThreadLocal<>();

    static {
        SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("easydao-config.xml"));
    }

    /**
     * 获取SqlSessionFactory对象
     *
     * @return SqlSessionFactory对象
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        return SQL_SESSION_FACTORY;
    }

    /**
     * 获取sqlSession对象
     * @return 对象
     */
    public static SqlSession getSqlSession(){
        SqlSession sqlSession = THREAD_LOCAL.get();
        if (sqlSession == null){
            sqlSession = SQL_SESSION_FACTORY.openSession();
            THREAD_LOCAL.set(sqlSession);
        }
        return sqlSession;
    }

    /**
     * 提交
     */
    public static void commit(){
        SqlSession sqlSession = THREAD_LOCAL.get();
        if (sqlSession != null){
            sqlSession.commit();
        }
    }

    /**
     * 关闭sqlSession对象
     * @param sqlSession 对象
     */
    public static void close(SqlSession sqlSession){
        if (sqlSession != null){
            THREAD_LOCAL.remove();
            sqlSession.close();
        }
    }
}
