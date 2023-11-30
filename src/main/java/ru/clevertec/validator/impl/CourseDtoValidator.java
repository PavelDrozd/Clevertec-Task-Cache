package ru.clevertec.validator.impl;

import ru.clevertec.data.CourseDto;
import ru.clevertec.exception.ValidationException;
import ru.clevertec.validator.ObjectValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class CourseDtoValidator implements ObjectValidator<CourseDto> {

    private final Validator validator;

    private static CourseDtoValidator INSTANCE;

    private CourseDtoValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static synchronized CourseDtoValidator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CourseDtoValidator();
        }
        return INSTANCE;
    }

    @Override
    public void validate(CourseDto courseDto) {
        Set<ConstraintViolation<CourseDto>> violations = validator.validate(courseDto);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<CourseDto> violation : violations) {
                throw new ValidationException(violation.getMessage());
            }
        }
    }
}
