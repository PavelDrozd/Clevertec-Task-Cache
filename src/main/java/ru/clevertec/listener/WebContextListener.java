package ru.clevertec.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.config.AppConfiguration;
import ru.clevertec.controller.CourseRestController;
import ru.clevertec.dao.connection.DataSourceManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class WebContextListener implements ServletContextListener {

    private static AnnotationConfigApplicationContext context;

    private DataSourceManager dataSourceManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("WebContextListener init");
        context = new AnnotationConfigApplicationContext(AppConfiguration.class);

        CourseRestController courseRestController = context.getBean(CourseRestController.class);
        ServletContext servletContext = sce.getServletContext();

        servletContext.addServlet("course", courseRestController)
                .addMapping("/courses");

        dataSourceManager = context.getBean(DataSourceManager.class);
        dataSourceManager.initDataBase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("WebContextListener destroy");

        dataSourceManager.dropDataBase();
        dataSourceManager.close();

        context.close();
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
