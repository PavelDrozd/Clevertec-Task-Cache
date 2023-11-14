package ru.clevertec.data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CourseDto(

        UUID id,

        String name,

        String info,

        BigDecimal cost,

        BigDecimal discount,

        LocalDate start,

        Duration duration,

        List<String> lessonTopics) {
}
