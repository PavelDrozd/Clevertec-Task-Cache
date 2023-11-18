package ru.clevertec.dao.factory;

import ru.clevertec.dao.CourseDao;
import ru.clevertec.dao.connection.DataSourceManager;
import ru.clevertec.dao.impl.CourseDaoImpl;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum used as factory for initialize DAO implementation classes.
 * Enum values are available globally, and used as a singleton.
 */
public enum DaoFactory {
    INSTANCE;

    /** Map for store DAO classes */
    private final Map<Class<?>, Object> map;

    /**
     * Initialize HashMap, get DataSource instance from DataSourceManager and use in for initialize DAO implementation
     * classes, put them into the map.
     */
    DaoFactory() {
        map = new HashMap<>();
        DataSource dataSource = DataSourceManager.INSTANCE.getDataSource();

        map.put(CourseDao.class, new CourseDaoImpl(dataSource));
    }

    /**
     * Public method for get DAO class from factory.
     *
     * @param clazz expected object class type of T
     * @param <T>   expected type T
     * @return object of type T
     */
    @SuppressWarnings("unchecked")
    public <T> T getDao(Class<T> clazz) {
        T dao = (T) map.get(clazz);
        if (dao == null) {
            throw new RuntimeException("Class " + clazz + " is not constructed.");
        }
        return dao;
    }
}
