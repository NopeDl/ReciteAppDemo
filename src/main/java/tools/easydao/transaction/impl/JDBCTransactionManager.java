package tools.easydao.transaction.impl;

import tools.easydao.transaction.TransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author yeyeye
 */
public class JDBCTransactionManager implements TransactionManager {
    private Connection connection;

    private DataSource dataSource;

    private boolean autoCommit;

    public JDBCTransactionManager(DataSource dataSource, boolean autoCommit) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollBack() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        if (connection == null) {
            openConnection(autoCommit);
        }
        return connection;
    }

    @Override
    public void openConnection() {
        openConnection(false);
    }

    @Override
    public void openConnection(boolean autoCommit) {
        this.autoCommit = autoCommit;
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(this.autoCommit);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
