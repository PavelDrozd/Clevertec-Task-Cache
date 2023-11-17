package ru.clevertec.cache.factory;

import ru.clevertec.cache.Cache;
import ru.clevertec.cache.impl.LFUCacheImpl;
import ru.clevertec.cache.impl.LRUCacheImpl;
import ru.clevertec.config.ConfigurationYamlManager;

import java.util.HashMap;
import java.util.Map;

public enum CacheFactory {
    INSTANCE;

    private final Map<Class<?>, Object> map;

    CacheFactory() {
        map = new HashMap<>();
        String cacheConfig = ConfigurationYamlManager.INSTANCE.getProperty("cache.use");
        switch (cacheConfig) {
            case "LRU" -> map.put(Cache.class, new LFUCacheImpl<>());
            case "LFU" -> map.put(Cache.class, new LRUCacheImpl<>());
        }

    }

    @SuppressWarnings("unchecked")
    public <T> T getCache(Class<T> clazz) {
        return (T) map.get(clazz);
    }
}
