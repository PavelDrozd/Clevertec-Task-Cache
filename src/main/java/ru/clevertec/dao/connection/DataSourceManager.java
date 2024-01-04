package ru.clevertec.dao.connection;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.exception.ApplicationException;
import ru.clevertec.exception.InputStreamException;
import ru.clevertec.reader.DataInputStreamReader;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
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
@Component
@RequiredArgsConstructor
public class DataSourceManager {

    /** Driver property in configuration file. */
    @Value("${database.driver}")
    private String DB_DRIVER;
    /** Url property in configuration file. */
    @Value("${database.url}")
    private String DB_URL;
    /** User property in configuration file. */
    @Value("${database.user}")
    private String DB_USER;
    /** Password property in configuration file. */
    @Value("${database.password}")
    private String DB_PASSWORD;
    /** Min pool size property in configuration file. */
    @Value("#{T(java.lang.Integer).parseInt('${database.min}')}")
    private Integer DB_MIN_POOL;
    /** Max pool size property in configuration file. */
    @Value("#{T(java.lang.Integer).parseInt('${database.max}')}")
    private Integer DB_MAX_POOL;
    /** Auto commit property in configuration file. */
    @Value("#{T(java.lang.Boolean).parseBoolean('${database.autocommit}')}")
    private Boolean DB_AUTO_COMMIT;
    /** Timeout property in configuration file. */
    @Value("#{T(java.lang.Integer).parseInt('${database.timeout}')}")
    private Integer DB_LOGIN_TIMEOUT;
    /** Database auto initialize property in configuration file. */
    @Value("#{T(java.lang.Boolean).parseBoolean('${database.auto_init}')}")
    private Boolean AUTO_INIT;
    /** Database initialize data property in configuration file. */
    @Value("#{T(java.lang.Boolean).parseBoolean('${database.add_data}')}")
    private Boolean ADD_DATA;
    /** Schema sql file path property in configuration file. */
    @Value("${database.sql.schema}")
    private String SCHEMA_SQL;
    /** Data sql file path property in configuration file. */
    @Value("${database.sql.data}")
    private String DATA_SQL;
    /** Drop sql file path property in configuration file. */
    @Value("${database.sql.drop}")
    private String DROP_SQL;

    /** HikariDataSource used as main DataSource class. */
    private HikariDataSource dataSource;

    private final DataInputStreamReader dataInputStreamReader;

    @PostConstruct
    public void init(){
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(DB_DRIVER);
        hikariConfig.setJdbcUrl(DB_URL);
        hikariConfig.setUsername(DB_USER);
        hikariConfig.setPassword(DB_PASSWORD);
        hikariConfig.setMinimumIdle(DB_MIN_POOL);
        hikariConfig.setMaximumPoolSize(DB_MAX_POOL);
        hikariConfig.setAutoCommit(DB_AUTO_COMMIT);
        hikariConfig.setConnectionTimeout(DB_LOGIN_TIMEOUT);

        dataSource = new HikariDataSource(hikariConfig);
    }

    /** Get instance of DataSource method. */
    public DataSource getDataSource() {
        return dataSource;
    }

    /** Close data source method. */
    public void close() {
        log.debug("DataSourceManager close method");
        dataSource.close();
        deregisterDriver();
    }

    /** Initialize database method. */
    public void initDataBase() {
        if (AUTO_INIT) {
            executeSqlByPath(SCHEMA_SQL);
        }
        if (ADD_DATA) {
            executeSqlByPath(DATA_SQL);
        }
    }

    /** Drop database method. */
    public void dropDataBase() {
        if (AUTO_INIT) {
            executeSqlByPath(DROP_SQL);
        }
    }

    private void executeSqlByPath(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            executeSqlFileByInputStream(inputStream);
        } catch (IOException e) {
            throw new InputStreamException(e);
        }
    }

    private void executeSqlFileByInputStream(InputStream inputStream) {
        log.debug("DataSourceManager executeSqlFile method");
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = dataInputStreamReader.getString(inputStream);

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            throw new ApplicationException("Exception while trying execute SQL: " + e);
        }
    }

    private void deregisterDriver() {
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
