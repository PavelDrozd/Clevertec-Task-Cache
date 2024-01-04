package ru.clevertec.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.data.Paging;

@Component
@RequiredArgsConstructor
public class Pagination {

    @Value("#{T(java.lang.Long).parseLong('${pagination.limit}')}")
    private long DEFAULT_LIMIT;

    public Paging getPaging(long limit, long offset, long currentPage, long totalEntities) {
        long definedLimit = getValidatedLimit(limit, totalEntities);
        long definedCurrentPage = getValidCurrentPage(currentPage);
        long definedOffset = getValidOffset(offset, definedLimit, definedCurrentPage);
        long totalPages = getTotalPages(totalEntities, definedLimit);
        long page = getPage(currentPage, totalPages);
        return new Paging(definedLimit, definedOffset, page, totalPages);
    }

    private long getValidatedLimit(long limit, long totalEntities) {
        if (limit <= 0) {
            return DEFAULT_LIMIT;
        } else {
            return Math.min(limit, totalEntities);
        }
    }

    private long getValidCurrentPage(long currentPage) {
        if (currentPage < 0) {
            return 0;
        } else {
            return currentPage;
        }
    }

    private long getValidOffset(long offset, long limit, long currentPage) {
        if (offset <= 0 && currentPage > 0) {
            return (currentPage - 1) * limit;
        } else {
            return offset;
        }
    }

    private long getTotalPages(long totalEntities, long limit) {
        long pages = totalEntities / limit;
        int additionalPage = (totalEntities - (pages * limit) > 0) ? 1 : 0;
        return pages + additionalPage;
    }

    private long getPage(long currentPage, long totalPages) {
        long page;
        if (currentPage <= 0) {
            page = 1;
        } else {
            page = Math.min(currentPage, totalPages);
        }
        return page;
    }
}
