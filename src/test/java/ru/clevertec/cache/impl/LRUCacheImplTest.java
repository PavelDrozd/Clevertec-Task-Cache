package ru.clevertec.cache.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.cache.Cache;
import ru.clevertec.data.CourseTestData;
import ru.clevertec.entity.Course;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LRUCacheImplTest {

    private final Cache<UUID, Course> cache = new LRUCacheImpl<>(2);

    @Test
    void putShouldReturnExpectedCourseAfterPutCoursesOverCacheSize() {
        // given
        Course courseForPut1 = CourseTestData.builder()
                .withId(UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f1"))
                .withName("Java junior course")
                .build().buildCourse();
        Course courseForPut2 = CourseTestData.builder()
                .withId(UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f2"))
                .withName("Java advanced course")
                .build().buildCourse();
        Course courseForPut3 = CourseTestData.builder().build().buildCourse();
        UUID uuid1 = courseForPut1.getId();
        UUID uuid2 = courseForPut2.getId();
        UUID uuid3 = courseForPut3.getId();
        Course expected = CourseTestData.builder().build().buildCourse();

        // when
        cache.put(uuid1, courseForPut1);
        cache.put(uuid2, courseForPut2);
        cache.put(uuid3, courseForPut3);
        Course actual = cache.get(uuid3).orElseThrow();

        // then
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void getShouldReturnExpectedCourse() {
        // given
        Course course = CourseTestData.builder().build().buildCourse();
        UUID uuid = course.getId();
        Course expected = CourseTestData.builder().build().buildCourse();

        // when
        cache.put(uuid, course);
        Course actual = cache.get(uuid).orElseThrow();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void sizeShouldReturnExpectedCurrentCacheSize() {
        // given
        Course courseForPut1 = CourseTestData.builder()
                .withId(UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f1"))
                .withName("Java junior course")
                .build().buildCourse();
        Course courseForPut2 = CourseTestData.builder().build().buildCourse();
        UUID uuid1 = courseForPut1.getId();
        UUID uuid2 = courseForPut2.getId();
        int expected = 2;

        // when
        cache.put(uuid1, courseForPut1);
        cache.put(uuid2, courseForPut2);
        int actual = cache.size();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void isEmptyShouldReturnTrue() {
        // given
        boolean expected = true;

        // when
        boolean actual = cache.isEmpty();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void removeShouldReturnOptionalEmpty() {
        // given
        Course course = CourseTestData.builder().build().buildCourse();
        UUID uuid = course.getId();

        // when
        cache.put(uuid, course);
        cache.remove(uuid);
        Optional<Course> actual = cache.get(uuid);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void clearShouldReturnIsEmptyTrue() {
        // given
        Course course = CourseTestData.builder().build().buildCourse();
        UUID uuid = course.getId();

        // when
        cache.put(uuid, course);
        cache.clear();
        Optional<Course> actual = cache.get(uuid);

        // then
        assertThat(actual).isEmpty();
    }
}