package ru.clevertec.dao;

import java.util.List;
import java.util.Optional;

/**
 * Abstract interface for CRUD operations.
 *
 * @param <K> key - used as a primary key parameter.
 * @param <T> type - is the type of class used.
 */
public interface AbstractDao<K, T> {

    /**
     * Accepts a class, transform it for send in DAO.
     *
     * @param t Accepts a class of type T to create it.
     * @return new created class of type T.
     */
    T save(T t);

    /**
     * Return all object of type T.
     *
     * @return list of objects of type T.
     */
    List<T> findAll();

    /**
     * Find object of type T by object K used as primary key and return it.
     *
     * @param id accept object of type K used as primary key.
     * @return object of type T.
     */
    Optional<T> findById(K id);

    /**
     * Delete existing object by primary key.
     *
     * @param id accept object of type K used as primary key.
     */
    boolean deleteById(K id);
}