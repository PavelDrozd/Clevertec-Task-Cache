package ru.clevertec.data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public record CourseDto(

        String name,

        String info,

        BigDecimal cost,

        BigDecimal discount,

        LocalDate start,

        Duration duration,

        List<String> lessonTopics) {
}
