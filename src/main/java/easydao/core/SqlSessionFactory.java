package easydao.core;

import easydao.pojo.SqlStatement;
import easydao.transaction.TransactionManager;
import easydao.type.TypeHandleRegistry;

import java.util.Map;

public class SqlSessionFactory {

    /**
     * 事务管理器
     */
    private TransactionManager transactionManager;


    /**
     * mapper
     */
    private Map<String, SqlStatement> statementMappers;

    private TypeHandleRegistry typeHandleRegistry;

    /**
     * 开启SQLSession
     *
     * @return SQLSession对象
     */
    public SqlSession openSession() {
        return openSession(false);
    }

    public SqlSession openSession(boolean autoCommit) {
        //打开连接
        transactionManager.openConnection(autoCommit);

        return new SqlSession(this);
    }

    public SqlSessionFactory() {
    }

    public SqlSessionFactory(TransactionManager transactionManager, Map<String, SqlStatement> statementMappers,TypeHandleRegistry typeHandleRegistry) {
        this.transactionManager = transactionManager;
        this.statementMappers = statementMappers;
        this.typeHandleRegistry = typeHandleRegistry;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Map<String, SqlStatement> getStatementMappers() {
        return statementMappers;
    }

    public void setStatementMappers(Map<String, SqlStatement> statementMappers) {
        this.statementMappers = statementMappers;
    }

    public TypeHandleRegistry getTypeHandleRegistry() {
        return typeHandleRegistry;
    }

    public void setTypeHandleRegistry(TypeHandleRegistry typeHandleRegistry) {
        this.typeHandleRegistry = typeHandleRegistry;
    }
}
