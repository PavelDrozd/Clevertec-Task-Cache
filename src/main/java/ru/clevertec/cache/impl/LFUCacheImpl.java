package ru.clevertec.cache.impl;

import lombok.Getter;
import lombok.Setter;
import ru.clevertec.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class implementation of Cache by using LFU(Least Frequently Used) algorithm.
 *
 * @param <K> key - used as a primary key parameter.
 * @param <T> type - is the type of class used.
 */
public class LFUCacheImpl<K, T> implements Cache<K, T> {

    /**
     * Class used as holder of object of type T and count used frequency.
     *
     * @param <T> type - is the type of class used.
     */
    @Getter
    @Setter
    static class CacheEntry<T> {
        private T t;
        private int frequency;

        private CacheEntry() {
        }
    }

    /** Capacity of cached objects. */
    private final int initialCapacity;

    /** Cache map for hold objects of type T and keys K. */
    private final Map<K, CacheEntry<T>> cacheMap = new LinkedHashMap<>();

    /** Constructor accept int value of cache capacity. */
    public LFUCacheImpl(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    /**
     * Method check cache capacity and put new objects in cache map.
     *
     * @param key  expected object type of K.
     * @param data expected object to save in cache.
     */
    @Override
    public void put(K key, T data) {
        if (isFull()) {
            K entryKeyToBeRemoved = getKeyToRemove();
            cacheMap.remove(entryKeyToBeRemoved);
        }
        CacheEntry<T> temp = buildCacheEntry(data);
        cacheMap.put(key, temp);
    }

    /**
     * Method get object from cache.
     *
     * @param key expected object type of K.
     * @return optional object type of T from cache.
     */
    @Override
    public Optional<T> get(K key) {
        if (cacheMap.containsKey(key)) {
            CacheEntry<T> temp = cacheMap.get(key);
            temp.frequency++;
            cacheMap.put(key, temp);
            return Optional.of(temp.t);
        }
        return Optional.empty();
    }

    /**
     * Method return current size of cache.
     *
     * @return int value of size.
     */
    @Override
    public int size() {
        return cacheMap.size();
    }

    /**
     * Method check if cache is empty.
     *
     * @return boolean result is empty.
     */
    @Override
    public boolean isEmpty() {
        return cacheMap.isEmpty();
    }

    /**
     * Method remove object from cache by key K.
     *
     * @param key expected object type of K.
     */
    @Override
    public void remove(K key) {
        cacheMap.remove(key);
    }

    /**
     * Method clear all cache.
     */
    @Override
    public void clear() {
        cacheMap.clear();
    }

    /**
     * Method checks whether the cache is full.
     *
     * @return boolean result if full.
     */
    private boolean isFull() {
        return cacheMap.size() == initialCapacity;
    }

    /**
     * Method build new CacheEntry with object of type T and set default frequency (0).
     *
     * @param data expected object type of T for save in cache.
     * @return CacheEntry with object type of T.
     */
    private CacheEntry<T> buildCacheEntry(T data) {
        CacheEntry<T> temp = new CacheEntry<>();
        temp.setT(data);
        temp.setFrequency(0);
        return temp;
    }

    /**
     * Method find object in cache with lower frequency.
     *
     * @return object type of K used as key.
     */
    private K getKeyToRemove() {
        K key = null;
        int minFreq = Integer.MAX_VALUE;
        for (Map.Entry<K, CacheEntry<T>> entry : cacheMap.entrySet()) {
            if (minFreq > entry.getValue().frequency) {
                key = entry.getKey();
                minFreq = entry.getValue().frequency;
            }
        }
        return key;
    }

}
