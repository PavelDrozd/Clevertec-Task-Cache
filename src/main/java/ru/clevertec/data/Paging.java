package ru.clevertec.data;

public record Paging(

        long limit,

        long offset,

        long page,

        long totalPages) {
}
