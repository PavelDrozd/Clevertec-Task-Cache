package ru.clevertec.writer.factory;

import ru.clevertec.writer.CoursePdfWriter;
import ru.clevertec.writer.impl.CoursePdfWriterImpl;

import java.util.HashMap;
import java.util.Map;

public enum WriterFactory {
    INSTANCE;

    private final Map<Class<?>, Object> map;

    WriterFactory() {
        map = new HashMap<>();
        map.put(CoursePdfWriter.class, new CoursePdfWriterImpl());

    }

    @SuppressWarnings("unchecked")
    public <T> T getWriter(Class<T> clazz) {
        return (T) map.get(clazz);
    }
}
