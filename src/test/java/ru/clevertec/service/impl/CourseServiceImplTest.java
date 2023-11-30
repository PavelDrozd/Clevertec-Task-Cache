package ru.clevertec.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.dao.CourseDao;
import ru.clevertec.data.CourseDto;
import ru.clevertec.data.CourseDtoTestBuilder;
import ru.clevertec.data.CourseTestBuilder;
import ru.clevertec.entity.Course;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.mapper.CourseMapper;

import java.util.List;
import java.util.Optional;
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

    @Captor
    private ArgumentCaptor<Course> captor;

    @Test
    void createShouldReturnExpectedCourseDto() {
        // given
        CourseDto courseDto = CourseDtoTestBuilder.builder().build().buildCourseDto();
        Course course = CourseTestBuilder.builder().build().buildCourse();
        UUID expected = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

        Mockito.when(courseMapper.toCourse(courseDto)).thenReturn(course);
        Mockito.when(courseDao.create(course)).thenReturn(course);

        // when
        UUID actual = courseService.create(courseDto);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getAllShouldReturnExpectedListOfCourseDtos() {
        // given
        List<Course> courses = CourseTestBuilder.builder().build().buildListOfCourses();
        Course course = CourseTestBuilder.builder().build().buildCourse();
        CourseDto courseDto = CourseDtoTestBuilder.builder().build().buildCourseDto();
        List<CourseDto> expected = CourseDtoTestBuilder.builder().build().buildListOfCourseDtos();

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
        Course course = CourseTestBuilder.builder().build().buildCourse();
        CourseDto expected = CourseDtoTestBuilder.builder().build().buildCourseDto();

        Mockito.when(courseDao.findById(uuid)).thenReturn(Optional.of(course));
        Mockito.when(courseMapper.toCourseDto(course)).thenReturn(expected);

        // when
        CourseDto actual = courseService.getById(uuid);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getByIdShouldThrowNotFoundException() {
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
        CourseDto courseDto = CourseDtoTestBuilder.builder().build().buildCourseDto();
        Course expected = CourseTestBuilder.builder().build().buildCourse();

        // when
        courseService.update(uuid, courseDto);

        // then
        verify(courseDao)
                .update(captor.capture());
        Course actual = captor.getValue();

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Course.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Course.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Course.Fields.info, expected.getInfo())
                .hasFieldOrPropertyWithValue(Course.Fields.cost, expected.getCost())
                .hasFieldOrPropertyWithValue(Course.Fields.discount, expected.getDiscount())
                .hasFieldOrPropertyWithValue(Course.Fields.start, expected.getStart())
                .hasFieldOrPropertyWithValue(Course.Fields.duration, expected.getDuration());
    }

    @Test
    void deleteShouldInvokeDaoMethodDeleteOneTime() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

        Mockito.when(courseDao.deleteById(uuid)).thenReturn(true);

        // when
        courseService.delete(uuid);

        // then
        verify(courseDao).deleteById(uuid);
    }

    @Test
    void deleteShouldThrowNotFoundException() {
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