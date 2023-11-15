package ru.clevertec.data;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
public class CourseDtoTestData {

    @Builder.Default
    private UUID id = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

    @Builder.Default
    private String name = "Java developer";

    @Builder.Default
    private String info = "Long time courses for Java developer.";

    @Builder.Default
    private BigDecimal cost = BigDecimal.valueOf(2000);

    @Builder.Default
    private BigDecimal discount = BigDecimal.valueOf(100);

    @Builder.Default
    private LocalDate start = LocalDate.of(2023, 10, 15);

    @Builder.Default
    private Duration duration = Duration.ofDays(180);

    @Builder.Default
    private List<String> lessonTopics = List.of("OOP", "Java installation", "Git", "Gradle", "Collections", "Concurrency", "Servlets");

    public CourseDto buildCourseDto() {
        return new CourseDto(name, info, cost, discount, start, duration, lessonTopics);
    }

    public List<CourseDto> buildListOfCourseDtos() {
        return List.of(buildCourseDto());
    }
}