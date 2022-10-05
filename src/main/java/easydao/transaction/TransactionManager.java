package easydao.transaction;

import java.sql.Connection;

public interface TransactionManager {
    /**
     * �ύ
     */
    void commit();

    /**
     * �ر�
     */
    void close();

    /**
     * �ع�
     */
    void rollBack();

    /**
     * ��ȡ����
     */
    Connection getConnection();

    /**
     * ������
     */
    void openConnection();

    /**
     * �Զ��������
     * @param autoCommit �Ƿ��Զ��ύ
     */
    void openConnection(boolean autoCommit);
}
