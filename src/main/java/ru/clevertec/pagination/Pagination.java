package ru.clevertec.pagination;

import ru.clevertec.config.ConfigurationYamlManager;
import ru.clevertec.data.Paging;

public class Pagination {

    private static final long DEFAULT_LIMIT = Long.parseLong(
            ConfigurationYamlManager.INSTANCE.getProperty("pagination.limit"));

    private static Pagination INSTANCE;

    public static synchronized Pagination getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Pagination();
        }
        return INSTANCE;
    }


    public Paging getPaging(long limit, long offset, long currentPage, long totalEntities) {
        long definedLimit = getValidatedLimit(limit, totalEntities);
        long totalPages = getTotalPages(totalEntities, limit);
        long page = getPage(currentPage, totalPages);
        return new Paging(definedLimit, offset, page, totalPages);
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

    private long getValidatedLimit(long limit, long totalEntities) {
        if (limit <= 0) {
            return DEFAULT_LIMIT;
        } else {
            return Math.min(limit, totalEntities);
        }
    }

    private static long getTotalPages(long totalEntities, long limit) {
        long pages = totalEntities / limit;
        int additionalPage = (totalEntities - (pages * limit) > 0) ? 1 : 0;
        return pages + additionalPage;
    }
}
