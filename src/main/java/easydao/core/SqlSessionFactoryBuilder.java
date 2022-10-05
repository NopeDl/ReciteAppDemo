package easydao.core;

import easydao.datasource.PooledDataSource;
import easydao.datasource.UnPooledDataSource;
import easydao.pojo.SqlStatement;
import easydao.transaction.TransactionManager;
import easydao.transaction.impl.JDBCTransactionManager;
import easydao.type.TypeHandleRegistry;
import easydao.utils.Resources;
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
            //����XML
            SAXReader reader = new SAXReader();
            Document document = reader.read(input);
            Element environments = (Element) document.selectSingleNode("/configuration/environments");
            String defaultEnvironmentName = (environment == null ? environments.attributeValue("default") : environment);
            //��ȡ������ǩԪ��
            List<Node> nodes = document.selectNodes("/configuration/environments/environment");
            Element environmentElm = null;
            for (Node node : nodes) {
                Element elm = (Element) node;
                if (defaultEnvironmentName.equals(elm.attributeValue("id"))) {
                    environmentElm = elm;
                    break;
                }
            }
            //��ȡ�����������ǩԪ��
            Element transactionManagerElm = environmentElm.element("transactionManager");
            //��ȡ����Դ��ǩԪ��
            Element dataSourceElm = environmentElm.element("dataSource");
            //��������������Դ
            DataSource dataSource = getDataSource(dataSourceElm);
            //�������������������
            transactionManager = getTransactionManager(transactionManagerElm, dataSource);
            //����������statementMappers
            //��ȡMapper��ǩԪ��
            Element mappers = (Element) document.selectSingleNode("/configuration/mappers");
            statementMappers = getMappers(mappers);
            //��������ת����
            typeHandleRegistry = new TypeHandleRegistry();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new SqlSessionFactory(transactionManager, statementMappers, typeHandleRegistry);
    }

    /**
     * ��ȡ����Դ
     *
     * @param dataSourceElm ����Դ��ǩԪ��
     * @return ����Դ
     */
    private DataSource getDataSource(Element dataSourceElm) {
        String type = dataSourceElm.attributeValue("type").toUpperCase();
        //��ȡproperty
        Map<String, String> properties = new HashMap<>();
        List<Element> propertiesElm = dataSourceElm.elements("property");
        for (Element elm : propertiesElm) {
            String name = elm.attributeValue("name");
            String value = elm.attributeValue("value");
            properties.put(name, value);
        }

        //����type������Ӧ����Դ����
        DataSource dataSource = null;
        String driver = properties.get("driver");
        String url = properties.get("url");
        String username = properties.get("username");
        String password = properties.get("password");
        if ("UNPOOLED".equals(type)) {
            //�����ӳ�
            dataSource = new UnPooledDataSource(driver, url, username, password);
        } else if ("POOLED".equals(type)) {
            //���ӳ�
            dataSource = new PooledDataSource(driver, url, username, password);
        } else {
            //����
        }
        return dataSource;
    }

    /**
     * ��ȡ�������������
     *
     * @param transactionManagerElm �����������ǩԪ��
     * @param dataSource            ����Դ
     * @return �������������
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
     * ��ȡstatementMappers����
     *
     * @param mappers mapperԪ�ر�ǩ
     * @return ����
     */
    private Map<String, SqlStatement> getMappers(Element mappers) {
        Map<String, SqlStatement> statementMap = new HashMap<>();
        //��ȡmappers�µ�mapper xml�ļ�������
        SAXReader saxReader = new SAXReader();
        List<Element> elements = mappers.elements();
        for (Element element : elements) {
            try {
                //��ȡ�ļ�·��
                String mapperXMLPath = element.attributeValue("resource");
                //����XML
                Document document = saxReader.read(Resources.getResourceAsStream(mapperXMLPath));
                //��ȡnamespace
                Element mapper = (Element) document.selectSingleNode("mapper");
                String namespace = mapper.attributeValue("namespace").trim();
                //��ȡSql��䲢��װ��SqlStatement����
                List<Element> sqlElms = mapper.elements();
                for (Element sqlElm : sqlElms) {
                    //��ȡsql id
                    String id = sqlElm.attributeValue("id").trim();
                    String sqlId = namespace + "." + id;
                    //��ȡsql���
                    String sql = sqlElm.getTextTrim();
                    //��ȡresultType
                    String resultType = sqlElm.attributeValue("resultType");
                    //��װ
                    SqlStatement sqlStatement = new SqlStatement();
                    sqlStatement.setSql(sql);
                    sqlStatement.setResultType(resultType);
                    //����map��
                    statementMap.put(sqlId, sqlStatement);
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return statementMap;
    }


}
