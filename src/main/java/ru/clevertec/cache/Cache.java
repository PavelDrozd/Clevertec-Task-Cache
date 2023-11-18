package ru.clevertec.cache;

import java.util.Optional;

public interface Cache<K, T> {

    void put(K key, T type);

    Optional<T> get(K key);

    int size();

    boolean isEmpty();

    void remove(K key);

    void clear();
}
