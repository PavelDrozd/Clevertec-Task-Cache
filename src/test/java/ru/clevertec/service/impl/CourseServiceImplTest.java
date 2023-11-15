package ru.clevertec.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.dao.CourseDao;
import ru.clevertec.data.CourseDto;
import ru.clevertec.data.CourseDtoTestData;
import ru.clevertec.data.CourseTestData;
import ru.clevertec.entity.Course;
import ru.clevertec.exception.service.NotFoundException;
import ru.clevertec.mapper.CourseMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseDao courseDao;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void createShouldReturnExpectedCourseDto() {
        // given
        CourseDto courseDto = CourseDtoTestData.builder().build().buildCourseDto();
        Course course = CourseTestData.builder().build().buildCourse();
        CourseDto expected = CourseDtoTestData.builder().build().buildCourseDto();

        Mockito.when(courseMapper.toCourse(courseDto)).thenReturn(course);
        Mockito.when(courseDao.save(course)).thenReturn(course);
        Mockito.when(courseMapper.toCourseDto(course)).thenReturn(expected);

        // when
        CourseDto actual = courseService.create(courseDto);

        // then
        assertThat(actual)
                .isSameAs(expected);
    }

    @Test
    void getAllShouldReturnExpectedListOfCourseDtos() {
        // given
        List<Course> courses = CourseTestData.builder().build().buildListOfCourses();
        Course course = CourseTestData.builder().build().buildCourse();
        CourseDto courseDto = CourseDtoTestData.builder().build().buildCourseDto();
        List<CourseDto> expected = CourseDtoTestData.builder().build().buildListOfCourseDtos();

        Mockito.when(courseDao.findAll()).thenReturn(courses);
        Mockito.when(courseMapper.toCourseDto(course)).thenReturn(courseDto);

        // when
        List<CourseDto> actual = courseService.getAll();

        // then
        assertThat(actual)
                .hasSameElementsAs(expected);
    }

    @Test
    void getByIdShouldReturnExpectedCourse() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");
        Course course = CourseTestData.builder().build().buildCourse();
        CourseDto expected = CourseDtoTestData.builder().build().buildCourseDto();

        Mockito.when(courseDao.findById(uuid)).thenReturn(course);
        Mockito.when(courseMapper.toCourseDto(course)).thenReturn(expected);

        // when
        CourseDto actual = courseService.getById(uuid);

        // then
        assertThat(actual)
                .isSameAs(expected);
    }

    @Test
    void getByIdShouldThrowServiceNotFoundException() {
        // given
        UUID fakeUuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f0");
        String expected = "not found";

        // when
        Exception exception = assertThrows(NotFoundException.class, () -> courseService.getById(fakeUuid));
        String actual = exception.getMessage();

        // then
        assertThat(actual)
                .contains(expected);
    }

    @Test
    void updateShouldReturnExpectedCourseDto() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");
        CourseDto courseDto = CourseDtoTestData.builder().build().buildCourseDto();
        Course course = CourseTestData.builder().build().buildCourse();
        CourseDto expected = CourseDtoTestData.builder().build().buildCourseDto();

        Mockito.when(courseMapper.toCourse(courseDto)).thenReturn(course);
        Mockito.when(courseDao.save(course)).thenReturn(course);
        Mockito.when(courseMapper.toCourseDto(course)).thenReturn(expected);

        // when
        CourseDto actual = courseService.update(uuid, courseDto);

        // then
        assertThat(actual)
                .isSameAs(expected);
    }

    @Test
    void updateShouldThrowServiceNotFoundException() {
        // given
        UUID fakeUuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f0");
        CourseDto courseDto = CourseDtoTestData.builder().build().buildCourseDto();
        String expected = "not found";

        // when
        Exception exception = assertThrows(NotFoundException.class, () -> courseService.update(fakeUuid, courseDto));
        String actual = exception.getMessage();

        // then
        assertThat(actual)
                .contains(expected);
    }

    @Test
    void deleteShouldInvokeRepositoryMethodDeleteOneTime() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

        // when
        courseService.delete(uuid);

        // then
        verify(courseDao).deleteById(uuid);
    }

    @Test
    void deleteShouldThrowServiceNotFoundException() {
        // given
        UUID fakeUuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f0");
        String expected = "not found";

        // when
        Exception exception = assertThrows(NotFoundException.class, () -> courseService.delete(fakeUuid));
        String actual = exception.getMessage();

        // then
        assertThat(actual)
                .contains(expected);
    }
}