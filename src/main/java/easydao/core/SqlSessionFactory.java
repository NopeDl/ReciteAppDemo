package easydao.core;

import easydao.pojo.SqlStatement;
import easydao.transaction.TransactionManager;
import easydao.type.TypeHandleRegistry;

import java.util.Map;

public class SqlSessionFactory {

    /**
     * ���������
     */
    private TransactionManager transactionManager;


    /**
     * mapper
     */
    private Map<String, SqlStatement> statementMappers;

    private TypeHandleRegistry typeHandleRegistry;

    /**
     * ����SQLSession
     *
     * @return SQLSession����
     */
    public SqlSession openSession() {
        return openSession(false);
    }

    public SqlSession openSession(boolean autoCommit) {
        //������
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
