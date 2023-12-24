package ru.clevertec.service;

import java.util.List;

/**
 * This interface is abstract for CRUD operations.
 *
 * @param <K> key - used as a primary key parameter.
 * @param <T> type - is the type of class used.
 */
public interface AbstractService<K, T> {

    /**
     * Accepts a class, create it and send in database.
     *
     * @param t expected object of type T to create it.
     * @return K - key of new created object.
     */
    K create(T t);

    /**
     * Return all object of type T.
     *
     * @return list of objects of type T.
     */
    List<T> getAll();

    /**
     * Get object of type T by object K used as primary key and return it.
     *
     * @param id expected object of type K used as primary key.
     * @return object of type T.
     */
    T getById(K id);

    /**
     * Accepts a class, update it and send in DAO.
     *
     * @param id expected object of type K used as ID.
     * @param t expected object of type T.
     */
    T update(K id, T t);

    /**
     * Delete existing object by primary key.
     *
     * @param id expected object of type K used as primary key.
     */
    void delete(K id);

}
