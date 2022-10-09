package easydao.transaction;

import java.sql.Connection;

public interface TransactionManager {
    /**
     * 提交
     */
    void commit();

    /**
     * 关闭
     */
    void close();

    /**
     * 回滚
     */
    void rollBack();

    /**
     * 获取连接
     */
    Connection getConnection();

    /**
     * 打开连接
     */
    void openConnection();

    /**
     * 自定义打开连接
     * @param autoCommit 是否自动提交
     */
    void openConnection(boolean autoCommit);
}
