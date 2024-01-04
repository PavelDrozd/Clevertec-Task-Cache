package ru.clevertec.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.impl.LFUCacheImpl;
import ru.clevertec.cache.impl.LRUCacheImpl;
import ru.clevertec.dao.connection.DataSourceManager;
import ru.clevertec.entity.Course;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

@Configuration
@ComponentScan("ru/clevertec")
@EnableAspectJAutoProxy
@PropertySource("classpath:application.yml")
public class AppConfiguration {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        PropertySourcesPlaceholderConfigurer configure = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties yamlObject = Objects.requireNonNull(yaml.getObject(), "Yaml not found.");
        configure.setProperties(yamlObject);
        return configure;
    }

    @Bean
    public DataSource dataSource(DataSourceManager dataSourceManager) {
        return dataSourceManager.getDataSource();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
    }

    @Bean
    public Cache<UUID, Course> cache(@Value("#{T(java.lang.Integer).parseInt('${cache.size}')}") int initialCapacity,
                                     @Value("${cache.type}") String cacheType) {
        if (cacheType.equals("LRU")) {
            return new LRUCacheImpl<>(initialCapacity);
        } else {
            return new LFUCacheImpl<>(initialCapacity);
        }
    }
}
