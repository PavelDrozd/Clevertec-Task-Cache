package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Course {

    private UUID id;

    private String name;

    private String info;

    private BigDecimal cost;

    private BigDecimal discount;

    private LocalDate start;

    private Duration duration;

    private List<String> lessonTopics;
}
