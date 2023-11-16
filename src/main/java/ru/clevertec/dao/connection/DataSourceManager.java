package ru.clevertec.dao.connection;


import com.zaxxer.hikari.HikariDataSource;
import ru.clevertec.config.ConfigurationYamlManager;
import ru.clevertec.exception.ApplicationException;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * This class allows to create connection with database.
 * Enum values are available globally, and used as a singleton.
 */
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
        try {
            dataSource = new HikariDataSource();
            ConfigurationYamlManager yaml = ConfigurationYamlManager.INSTANCE;

            dataSource.setDriverClassName(yaml.getProperty(DB_DRIVER));
            dataSource.setJdbcUrl(yaml.getProperty(DB_URL));
            dataSource.setUsername(yaml.getProperty(DB_USER));
            dataSource.setPassword(yaml.getProperty(DB_PASSWORD));
            dataSource.setMinimumIdle(Integer.parseInt(yaml.getProperty(DB_MIN_POOL)));
            dataSource.setMaximumPoolSize(Integer.parseInt(yaml.getProperty(DB_MAX_POOL)));
            dataSource.setAutoCommit(Boolean.parseBoolean(yaml.getProperty(DB_AUTO_COMMIT)));
            dataSource.setLoginTimeout(Integer.parseInt(yaml.getProperty(DB_LOGIN_TIMEOUT)));
        } catch (SQLException e) {
            throw new ApplicationException("Exception connect to DataSource " + e);
        }
    }

    /** Public method for get instance of DataSource. */
    public DataSource getDataSource() {
        return dataSource;
    }

    /** Public method for close data source. */
    public void close() {
        dataSource.close();
    }
}
