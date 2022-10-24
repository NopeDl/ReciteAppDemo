package tools.easydao.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class PooledDataSource implements DataSource {
    private String url;

    private String username;

    private String password;

    private final DataSource dataSource;

    public PooledDataSource(String driver, String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        Properties properties = new Properties();
        //设置驱动名字
        properties.setProperty("driverClassName", driver);
        //url
        properties.setProperty("url", url);
        //用户名
        properties.setProperty("username", username);
        //密码
        properties.setProperty("password", password);
        //初始连接数
        properties.setProperty("initialSize", "5");
        //最大连接数
        properties.setProperty("maxActive", "10");
        //设置最大等待时间
        properties.setProperty("maxWait", "3000");
        properties.setProperty("minIdle", "3");

        try {
            this.dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
