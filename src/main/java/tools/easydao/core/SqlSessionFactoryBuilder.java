package tools.easydao.core;

import tools.easydao.datasource.PooledDataSource;
import tools.easydao.datasource.UnPooledDataSource;
import tools.easydao.pojo.SqlStatement;
import tools.easydao.transaction.TransactionManager;
import tools.easydao.transaction.impl.JDBCTransactionManager;
import tools.easydao.type.TypeHandleRegistry;
import tools.easydao.utils.Resources;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactoryBuilder() {

    }

    public SqlSessionFactory build(InputStream input) {
        return build(input, null);
    }

    public SqlSessionFactory build(InputStream input, String environment) {
        TransactionManager transactionManager = null;
        Map<String, SqlStatement> statementMappers = null;
        TypeHandleRegistry typeHandleRegistry = null;
        try {
            //解析XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(input);
            Element environments = (Element) document.selectSingleNode("/configuration/environments");
            String defaultEnvironmentName = (environment == null ? environments.attributeValue("default") : environment);
            //获取环境标签元素
            List<Node> nodes = document.selectNodes("/configuration/environments/environment");
            Element environmentElm = null;
            for (Node node : nodes) {
                Element elm = (Element) node;
                if (defaultEnvironmentName.equals(elm.attributeValue("id"))) {
                    environmentElm = elm;
                    break;
                }
            }
            //获取事务管理器标签元素
            Element transactionManagerElm = environmentElm.element("transactionManager");
            //获取数据源标签元素
            Element dataSourceElm = environmentElm.element("dataSource");
            //解析并创造数据源
            DataSource dataSource = getDataSource(dataSourceElm);
            //解析并创建事务管理器
            transactionManager = getTransactionManager(transactionManagerElm, dataSource);
            //解析并创建statementMappers
            //获取Mapper标签元素
            Element mappers = (Element) document.selectSingleNode("/configuration/mappers");
            statementMappers = getMappers(mappers);
            //创建类型转换器
            typeHandleRegistry = new TypeHandleRegistry();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new SqlSessionFactory(transactionManager, statementMappers, typeHandleRegistry);
    }

    /**
     * 获取数据源
     *
     * @param dataSourceElm 数据源标签元素
     * @return 数据源
     */
    private DataSource getDataSource(Element dataSourceElm) {
        String type = dataSourceElm.attributeValue("type").toUpperCase();
        //获取property
        Map<String, String> properties = new HashMap<>();
        List<Element> propertiesElm = dataSourceElm.elements("property");
        for (Element elm : propertiesElm) {
            String name = elm.attributeValue("name");
            String value = elm.attributeValue("value");
            properties.put(name, value);
        }

        //根据type创建对应数据源对象
        DataSource dataSource = null;
        String driver = properties.get("driver");
        String url = properties.get("url");
        String username = properties.get("username");
        String password = properties.get("password");
        if ("UNPOOLED".equals(type)) {
            //无连接池
            dataSource = new UnPooledDataSource(driver, url, username, password);
        } else if ("POOLED".equals(type)) {
            //连接池
            dataSource = new PooledDataSource(driver, url, username, password);
        } else {
            //其他
        }
        return dataSource;
    }

    /**
     * 获取事务管理器对象
     *
     * @param transactionManagerElm 事务管理器标签元素
     * @param dataSource            数据源
     * @return 事务管理器对象
     */
    private TransactionManager getTransactionManager(Element transactionManagerElm, DataSource dataSource) {
        String type = transactionManagerElm.attributeValue("type").toUpperCase();
        TransactionManager transactionManager = null;
        if ("JDBC".equals(type)) {
            //Ĭ�Ϲر��Զ��ύ
            transactionManager = new JDBCTransactionManager(dataSource, false);
        } else if ("MANAGED".equals(type)) {

        }
        return transactionManager;
    }

    /**
     * 获取statementMappers集合
     *
     * @param mappers mapper元素标签
     * @return 集合
     */
    private Map<String, SqlStatement> getMappers(Element mappers) {
        Map<String, SqlStatement> statementMap = new HashMap<>();
        //获取mappers下的mapper xml文件并解析
        SAXReader saxReader = new SAXReader();
        List<Element> elements = mappers.elements();
        for (Element element : elements) {
            try {
                //获取文件路径
                String mapperXMLPath = element.attributeValue("resource");
                //解析XML
                Document document = saxReader.read(Resources.getResourceAsStream(mapperXMLPath));
                //获取namespace
                Element mapper = (Element) document.selectSingleNode("mapper");
                String namespace = mapper.attributeValue("namespace").trim();
                //获取Sql语句并封装成SqlStatement对象
                List<Element> sqlElms = mapper.elements();
                for (Element sqlElm : sqlElms) {
                    //获取sql id
                    String id = sqlElm.attributeValue("id").trim();
                    String sqlId = namespace + "." + id;
                    //获取sql语句
                    String sql = sqlElm.getTextTrim();
                    //获取resultType
                    String resultType = sqlElm.attributeValue("resultType");
                    //封装
                    SqlStatement sqlStatement = new SqlStatement();
                    sqlStatement.setSql(sql);
                    sqlStatement.setResultType(resultType);
                    //放入map中
                    statementMap.put(sqlId, sqlStatement);
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return statementMap;
    }


}
