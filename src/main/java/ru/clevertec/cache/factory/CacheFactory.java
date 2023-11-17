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
        String cacheType = ConfigurationYamlManager.INSTANCE.getProperty("cache.type");
        int cacheSize = Integer.parseInt(ConfigurationYamlManager.INSTANCE.getProperty("cache.size"));
        switch (cacheType) {
            case "LRU" -> map.put(Cache.class, new LFUCacheImpl<>(cacheSize));
            case "LFU" -> map.put(Cache.class, new LRUCacheImpl<>(cacheSize));
        }

    }

    @SuppressWarnings("unchecked")
    public <T> T getCache(Class<T> clazz) {
        return (T) map.get(clazz);
    }
}