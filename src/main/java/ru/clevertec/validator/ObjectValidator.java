package ru.clevertec.validator;

public interface ObjectValidator<T> {

    void validate(T t);
}
