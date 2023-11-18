package ru.clevertec.mapper.factory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.HashMap;
import java.util.Map;

public enum MapperFactory {
    INSTANCE;

    private final Map<Class<?>, Object> map;

    MapperFactory() {
        map = new HashMap<>();
        map.put(ObjectMapper.class, new ObjectMapper()
                .findAndRegisterModules()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false));
        map.put(XmlMapper.class, new XmlMapper()
                        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .findAndRegisterModules()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false));
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> clazz) {
        return (T) map.get(clazz);
    }
}
