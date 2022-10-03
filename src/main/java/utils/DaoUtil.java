package utils;

import com.zz.core.SqlSessionFactory;
import com.zz.core.SqlSessionFactoryBuilder;
import com.zz.utils.Resources;

import java.io.IOException;
import java.io.InputStream;

public class DaoUtil {
    private static final SqlSessionFactoryBuilder sqlSessionFactoryBuilder;

    static {
        sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    }

    /**
     * 获取SqlSessionFactory对象
     *
     * @return SqlSessionFactory对象
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        InputStream input = Resources.getResourceAsStream("easydao-config.xml");
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(input);
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sqlSessionFactory;
    }
}
