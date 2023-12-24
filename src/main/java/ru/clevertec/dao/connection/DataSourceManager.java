package ru.clevertec.dao.connection;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.config.ConfigurationYamlManager;
import ru.clevertec.exception.ApplicationException;
import ru.clevertec.reader.DataInputStreamReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

/**
 * This class allows to create connection with database.
 * Enum values are available globally, and used as a singleton.
 */
@Slf4j
public enum DataSourceManager {
    INSTANCE;

    /** Driver property in configuration file. */
    private static final String DB_DRIVER = "database.driver";
    /** Url property in configuration file. */
    private static final String DB_URL = "database.url";
    /** User property in configuration file. */
    private static final String DB_USER = "database.user";
    /** Password property in configuration file. */
    private static final String DB_PASSWORD = "database.password";
    /** Min pool size property in configuration file. */
    private static final String DB_MIN_POOL = "database.min";
    /** Max pool size property in configuration file. */
    private static final String DB_MAX_POOL = "database.max";
    /** Auto commit property in configuration file. */
    private static final String DB_AUTO_COMMIT = "database.autocommit";
    /** Timeout property in configuration file. */
    private static final String DB_LOGIN_TIMEOUT = "database.timeout";

    /** HikariDataSource used as main DataSource class. */
    private final HikariDataSource dataSource;

    /**
     * Constructor initialize HikariDataSource and set properties in it by yaml configuration.
     */
    DataSourceManager() {
        ConfigurationYamlManager yaml = ConfigurationYamlManager.INSTANCE;
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(yaml.getProperty(DB_DRIVER));
        hikariConfig.setJdbcUrl(yaml.getProperty(DB_URL));
        hikariConfig.setUsername(yaml.getProperty(DB_USER));
        hikariConfig.setPassword(yaml.getProperty(DB_PASSWORD));
        hikariConfig.setMinimumIdle(Integer.parseInt(yaml.getProperty(DB_MIN_POOL)));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(yaml.getProperty(DB_MAX_POOL)));
        hikariConfig.setAutoCommit(Boolean.parseBoolean(yaml.getProperty(DB_AUTO_COMMIT)));
        hikariConfig.setConnectionTimeout(Integer.parseInt(yaml.getProperty(DB_LOGIN_TIMEOUT)));

        dataSource = new HikariDataSource(hikariConfig);
    }

    /** Public method for get instance of DataSource. */
    public DataSource getDataSource() {
        return dataSource;
    }

    /** Public method for close data source. */
    public void close() {
        log.debug("DataSourceManager close method");
        dataSource.close();
        deregisterDriver();
    }

    public void executeSqlFile(InputStream inputStream) {
        log.debug("DataSourceManager executeSqlFile method");
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = DataInputStreamReader.getString(inputStream);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new ApplicationException("Exception while trying execute SQL: " + e);
        }
    }

    private void deregisterDriver(){
        Enumeration<Driver> drivers = DriverManager.getDrivers();

        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();

            if (driver instanceof org.postgresql.Driver) {
                try {
                    DriverManager.deregisterDriver(driver);

                } catch (SQLException e) {
                    throw new ApplicationException(e);
                }
            }
        }
    }
}
