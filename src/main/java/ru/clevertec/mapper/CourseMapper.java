package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.data.CourseDto;
import ru.clevertec.entity.Course;
import ru.clevertec.service.CourseService;

/**
 * Interface for mapping course objects.
 */
@Mapper(componentModel = "spring", uses = CourseService.class)
public interface CourseMapper {

    /**
     * Accept DTO object and map it to entity.
     *
     * @param courseDto expected object type of CourseDto.
     * @return mapped Course object
     */
    @Mapping(target = "id", ignore = true)
    Course toCourse(CourseDto courseDto);

    /**
     * Accept entity object and map it to DTO.
     *
     * @param course expected object type of Course entity.
     * @return mapped CourseDto object.
     */
    CourseDto toCourseDto(Course course);
}
