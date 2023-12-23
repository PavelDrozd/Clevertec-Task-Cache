package ru.clevertec.controller.command.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.controller.command.CourseCommandResolver;
import ru.clevertec.controller.command.impl.CourseCommandResolverImpl;
import ru.clevertec.mapper.factory.MapperFactory;
import ru.clevertec.service.CourseService;
import ru.clevertec.service.factory.ServiceFactory;
import ru.clevertec.validator.impl.CourseDtoValidator;

import java.util.HashMap;
import java.util.Map;

public enum CommandResolverFactory {
    INSTANCE;

    private final Map<Class<?>, Object> map;

    CommandResolverFactory() {
        map = new HashMap<>();
        map.put(CourseCommandResolver.class, new CourseCommandResolverImpl(
                ServiceFactory.INSTANCE.getService(CourseService.class),
                MapperFactory.INSTANCE.getMapper(ObjectMapper.class),
                CourseDtoValidator.getInstance()));
    }

    @SuppressWarnings("unchecked")
    public <T> T getCommandResolver(Class<T> clazz) {
        return (T) map.get(clazz);
    }
}
