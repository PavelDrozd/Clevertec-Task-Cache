package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.dao.CourseDao;
import ru.clevertec.data.CourseDto;
import ru.clevertec.entity.Course;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.mapper.CourseMapper;
import ru.clevertec.service.CourseService;
import ru.clevertec.writer.CoursePdfWriter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of service interface for process course DTO objects.
 */
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    /** CourseDao is used to get objects from DAO module. */
    private final CourseDao courseDao;

    /** Mapper for mapping DTO and entity objects. */
    private final CourseMapper mapper;

    /**
     * Writer for create pdf files with course info.
     */
    private final CoursePdfWriter pdfWriter;

    /**
     * Method for create new DTO class and send it to DAO.
     *
     * @param courseDto expected object of type CourseDto to create it.
     * @return UUID of new created object.
     */
    @Override
    public UUID create(CourseDto courseDto) {
        log.debug("CourseServiceImpl create method: " + courseDto);
        Course course = mapper.toCourse(courseDto);
        Course saved = courseDao.create(course);
        return saved.getId();
    }

    /**
     * Method for getting all course DTO objects from DAO.
     *
     * @return List of CourseDto.
     */
    @Override
    public List<CourseDto> getAll() {
        log.debug("CourseServiceImpl get all method: ");
        return courseDao.findAll().stream().map(mapper::toCourseDto).toList();
    }

    /**
     * Method for getting all course DTO objects from DAO with expected limit.
     *
     * @param limit  expected number of courses for return.
     * @param offset expected started position of courses in database.
     * @return List of CourseDto.
     */
    @Override
    public List<CourseDto> getAll(long limit, long offset) {
        log.debug("CourseServiceImpl get all method with limit: " + limit + " offset: " + offset);
        return courseDao.findAll(limit, offset).stream().map(mapper::toCourseDto).toList();
    }

    /**
     * Method get course object from DAO by ID.
     *
     * @param id expected object of type K used as primary key.
     * @return CourseDto object.
     */
    @Override
    public CourseDto getById(UUID id) {
        log.debug("CourseServiceImpl get by ID method: " + id);
        Optional<Course> course = courseDao.findById(id);
        if (course.isEmpty()) {
            throw new NotFoundException("Course with id: " + id + " not found.");
        }
        CourseDto courseDto = mapper.toCourseDto(course.orElseThrow());
        pdfWriter.writePdf(courseDto);
        return courseDto;
    }

    /**
     * Method update course and send it in DAO.
     *
     * @param id        expected object of type K used as ID.
     * @param courseDto expected object of type T.
     */
    @Override
    public CourseDto update(UUID id, CourseDto courseDto) {
        log.debug("CourseServiceImpl update method: " + courseDto);
        Course course = Course.builder()
                .id(id)
                .name(courseDto.name())
                .info(courseDto.info())
                .cost(courseDto.cost())
                .discount(courseDto.discount())
                .start(courseDto.start())
                .duration(courseDto.duration())
                .build();
        Course updated = courseDao.update(course);
        return mapper.toCourseDto(updated);
    }

    /**
     * Delete existing object by UUID.
     *
     * @param id expected object of type K used as primary key.
     */
    @Override
    public void delete(UUID id) {
        log.debug("CourseServiceImpl delete by ID method: " + id);
        if (!courseDao.deleteById(id)) {
            throw new NotFoundException("Course with id " + id + " not found");
        }
    }

    /**
     * Counting all courses.
     *
     * @return count of courses.
     */
    @Override
    public long count() {
        log.debug("CourseServiceImpl count method");
        return courseDao.count();
    }
}
