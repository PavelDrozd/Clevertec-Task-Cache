package ru.clevertec.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.CourseDao;
import ru.clevertec.entity.Course;
import ru.clevertec.exception.service.ValidationException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of DAO interface for process course objects.
 * Using datasource for connect to the database.
 */
@RequiredArgsConstructor
public class CourseDaoImpl implements CourseDao {

    /** SELECT part of query with course parameters in the database. */
    private static final String SELECT_COURSE =
            "SELECT c.id, c.name, c.info, c.cost, c.discount, c.start, c.duration ";

    /** FROM part of query with courses table. */
    private static final String FROM_COURSES = "FROM courses c ";

    /** INSERT query to create a new row in the database. */
    private static final String INSERT_COURSE =
            "INSERT INTO courses (id, name, info, cost, discount, start, duration) VALUES ( ?, ?, ?, ?, ?, ?, ?)";

    /** SELECT query to find course by ID */
    private static final String SELECT_ACCOUNT_BY_ID = SELECT_COURSE + FROM_COURSES
                                                       + "WHERE c.id = ? AND deleted = false";

    /** SELECT query to find all courses from the database */
    private static final String SELECT_ALL_ACCOUNTS = SELECT_COURSE + FROM_COURSES;

    /** UPDATE query for set new values in fields of course entity. */
    private static final String UPDATE_COURSE =
            "UPDATE accounts SET name = ?, info = ?, cost = ?, discount = ?, start = ?, duration = ? WHERE id = ? AND deleted = false";

    /** DELETE query by set deleted value true in course row by ID from the database. */
    private static final String DELETE_ACCOUNT = "UPDATE accounts a SET  deleted = true WHERE a.id = ?";

    private final DataSource dataSource;

    /**
     * Method save entity in database.
     *
     * @param course expected object of type Course to save it.
     * @return saved Course object.
     */
    @Override
    public Course save(Course course) {
        if (findById(course.getId()).isEmpty()) {
            return create(course);
        } else {
            return update(course);
        }
    }

    /**
     * Method for find all course entities in database.
     *
     * @return List of Course objects.
     */
    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ACCOUNTS);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                courses.add(processCourse(result));
            }
            return courses;
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
    }

    /**
     * Method find entity in database by ID.
     *
     * @param id expected object of type UUID used as primary key.
     * @return object type of Course from database.
     */
    @Override
    public Optional<Course> findById(UUID id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_BY_ID);
            statement.setString(1, id.toString());
            ResultSet result = statement.executeQuery();
            Course course = new Course();
            if (result.next()) {
                course = processCourse(result);
            }
            return Optional.ofNullable(course);
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
    }

    /**
     * Method delete row in database by ID.
     *
     * @param id expected object of type UUID used as primary key.
     */
    @Override
    public boolean deleteById(UUID id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_ACCOUNT);
            statement.setString(1, id.toString());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
    }

    /**
     * Method process accepted Course entity and insert it in database.
     *
     * @param course expected object type of Course.
     * @return created object type of Course from database.
     */
    private Course create(Course course) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_COURSE);
            statement.setString(1, course.getId().toString());
            statement.setString(2, course.getName());
            statement.setString(3, course.getInfo());
            statement.setBigDecimal(4, course.getCost());
            statement.setBigDecimal(5, course.getDiscount());
            statement.setObject(6, course.getStart());
            statement.setLong(7, course.getDuration().toDays());
            statement.executeUpdate();
            return findById(course.getId()).orElseThrow();
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
    }

    /**
     * Method process accepted Course entity and update it in database.
     *
     * @param course expected object type of Course.
     * @return updated object type of Course from database.
     */
    private Course update(Course course) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE);
            statement.setString(1, course.getName());
            statement.setString(2, course.getInfo());
            statement.setBigDecimal(3, course.getCost());
            statement.setBigDecimal(4, course.getDiscount());
            statement.setObject(5, course.getStart());
            statement.setLong(6, course.getDuration().toDays());
            statement.setString(7, course.getId().toString());
            statement.executeUpdate();
            return findById(course.getId()).orElseThrow();
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
    }

    /**
     * Method for processing Course object to build a new.
     *
     * @param result expected ResultSet object.
     * @return new Course object.
     * @throws SQLException default exception by using ResultSet methods.
     */
    private Course processCourse(ResultSet result) throws SQLException {
        return Course.builder()
                .id(UUID.fromString(result.getString("id")))
                .name(result.getString("name"))
                .info(result.getString("info"))
                .cost(BigDecimal.valueOf(result.getDouble("cost")))
                .discount(BigDecimal.valueOf(result.getDouble("discount")))
                .start(LocalDate.parse(result.getString("start")))
                .duration(Duration.ofDays(result.getLong("duration")))
                .build();
    }
}
