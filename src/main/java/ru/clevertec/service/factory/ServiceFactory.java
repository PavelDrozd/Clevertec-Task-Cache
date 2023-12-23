package ru.clevertec.service.factory;

import ru.clevertec.dao.CourseDao;
import ru.clevertec.dao.factory.DaoFactory;
import ru.clevertec.mapper.CourseMapperImpl;
import ru.clevertec.service.CourseService;
import ru.clevertec.service.impl.CourseServiceImpl;
import ru.clevertec.writer.CoursePdfWriter;
import ru.clevertec.writer.factory.WriterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum used as factory for initialize Service implementation classes.
 * Enum values are available globally, and used as a singleton.
 */
public enum ServiceFactory {
    INSTANCE;

    /** Map for store Service classes */
    private final Map<Class<?>, Object> map;

    /**
     * Initialize HashMap, get DaoFactory instance and get DAO class for initialize Service implementation classes,
     * put them into the map.
     */
    ServiceFactory() {
        map = new HashMap<>();
        map.put(CourseService.class, new CourseServiceImpl(DaoFactory.INSTANCE.getDao(CourseDao.class),
                new CourseMapperImpl(),
                WriterFactory.INSTANCE.getWriter(CoursePdfWriter.class)));
    }

    /**
     * Public method for get service class from factory.
     *
     * @param clazz expected object class type of T
     * @param <T>   expected type T
     * @return object of type T
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) map.get(clazz);
    }
}
