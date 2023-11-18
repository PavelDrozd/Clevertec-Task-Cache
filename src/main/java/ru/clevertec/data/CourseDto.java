package ru.clevertec.data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

public record CourseDto(

        @NotBlank(message = "Name is blank")
        @NotEmpty(message = "Name is empty")
        @Size(max = 50, message = "Name size more than 50")
        @Pattern(regexp = "[a-zA-Z0-9()\\-,'\"\\s\\n.]+", message = "Name have inaccessible symbols")
        String name,

        @NotBlank(message = "Name is blank")
        @NotEmpty(message = "Name is empty")
        @Size(max = 300, message = "Name size more than 300")
        @Pattern(regexp = "[a-zA-Z0-9()\\-,'\"\\s\\n.]+", message = "Info have inaccessible symbols")
        String info,

        @NotNull(message = "Cost is null")
        @DecimalMin(value = "0", message = "Cost less than 0")
        BigDecimal cost,

        @NotNull(message = "Discount is null")
        @DecimalMin(value = "0")
        BigDecimal discount,

        @NotNull (message = "Start date is null")
        LocalDate start,

        @NotNull (message = "Duration is null")
        Duration duration) {
}
