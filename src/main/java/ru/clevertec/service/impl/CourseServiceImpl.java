package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.dao.CourseDao;
import ru.clevertec.data.CourseDto;
import ru.clevertec.entity.Course;
import ru.clevertec.exception.service.NotFoundException;
import ru.clevertec.mapper.CourseMapper;
import ru.clevertec.service.CourseService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    private final CourseMapper mapper;

    @Override
    public CourseDto create(CourseDto courseDto) {
        Course course = mapper.toCourse(courseDto);
        Course saved = courseDao.save(course);
        return mapper.toCourseDto(saved);
    }

    @Override
    public List<CourseDto> getAll() {
        return courseDao.findAll().stream().map(mapper::toCourseDto).toList();
    }

    @Override
    public CourseDto getById(UUID id) {
        Course course = courseDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Course with id: " + id + " not found."));
        return mapper.toCourseDto(course);
    }

    @Override
    public CourseDto update(UUID id, CourseDto courseDto) {
        if (courseDao.findById(id).isEmpty()){
            throw new NotFoundException("Course with id: " + id  + " not found.");
        }
        Course course = Course.builder()
                .id(id)
                .name(courseDto.name())
                .info(courseDto.info())
                .cost(courseDto.cost())
                .discount(courseDto.discount())
                .start(courseDto.start())
                .duration(courseDto.duration())
                .build();
        Course updated = courseDao.save(course);
        return mapper.toCourseDto(updated);
    }

    @Override
    public void delete(UUID id) {
        if (!courseDao.deleteById(id)){
            throw new NotFoundException("Course with id " + id + " not found");
        }

    }
}
