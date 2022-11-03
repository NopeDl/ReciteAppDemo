package tools.utils;

import tools.easydao.core.SqlSessionFactory;
import tools.easydao.core.SqlSessionFactoryBuilder;
import tools.easydao.utils.Resources;

import java.io.IOException;
import java.io.InputStream;

public class DaoUtil {
    private static final SqlSessionFactoryBuilder sqlSessionFactoryBuilder;

    private static SqlSessionFactory sqlSessionFactory = null;

    static {
        sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    }

    /**
     * 获取SqlSessionFactory对象
     *
     * @return SqlSessionFactory对象
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null){
            InputStream input = Resources.getResourceAsStream("easydao-config.xml");
            sqlSessionFactory = sqlSessionFactoryBuilder.build(input);
            try {
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sqlSessionFactory;
    }
}
