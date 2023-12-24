package ru.clevertec.listener;

import lombok.extern.slf4j.Slf4j;
import ru.clevertec.config.ConfigurationYamlManager;
import ru.clevertec.dao.connection.DataSourceManager;
import ru.clevertec.exception.InputStreamException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@WebListener
public class WebContextListener implements ServletContextListener {

    private static final String SCHEMA_SQL = "database.schema_sql";
    private static final String DATA_SQL = "database.data_sql";
    private static final String DROP_SQL = "database.drop_sql";

    private final DataSourceManager dataSourceManager = DataSourceManager.INSTANCE;

    private final ConfigurationYamlManager yaml = ConfigurationYamlManager.INSTANCE;

    private final boolean autoInit = Boolean.parseBoolean(yaml.getProperty("database.auto_init"));
    private final boolean addData = Boolean.parseBoolean(yaml.getProperty("database.add_data"));


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        if (autoInit) {
            String schemaSqlPath = yaml.getProperty(SCHEMA_SQL);
            executeSql(schemaSqlPath, servletContext);
        }
        if (addData) {
            String dataSqlPath = yaml.getProperty(DATA_SQL);
            executeSql(dataSqlPath, servletContext);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        if (autoInit) {
            String dropSqlPath = yaml.getProperty(DROP_SQL);
            executeSql(dropSqlPath, servletContext);
        }
        dataSourceManager.close();
    }

    private void executeSql(String path, ServletContext servletContext) {
        try (InputStream inputStream = servletContext.getResourceAsStream(path)) {
            dataSourceManager.executeSqlFile(inputStream);

        } catch (IOException e) {
            throw new InputStreamException(e);
        }
    }
}
