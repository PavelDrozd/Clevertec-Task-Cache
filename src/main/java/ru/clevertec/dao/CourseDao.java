package ru.clevertec.dao;

import ru.clevertec.entity.Course;

import java.util.List;
import java.util.UUID;

/**
 * DAO interface for course process Course entity.
 */
public interface CourseDao extends AbstractDao<UUID, Course> {

    List<Course> findAll(long limit, long offset);

    long count();
}
