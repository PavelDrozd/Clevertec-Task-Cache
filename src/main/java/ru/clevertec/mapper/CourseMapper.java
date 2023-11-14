package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.data.CourseDto;
import ru.clevertec.entity.Course;

/**
 * Interface for mapping course objects.
 */
@Mapper
public interface CourseMapper {

    /**
     * Accept DTO object and map it to entity.
     *
     * @param courseDto expected object type of CourseDto.
     * @return mapped Course object
     */
    Course toCourse(CourseDto courseDto);

    /**
     * Accept entity object and map it to DTO.
     *
     * @param course expected object type of Course entity.
     * @return mapped CourseDto object.
     */
    CourseDto toCourseDto(Course course);
}
