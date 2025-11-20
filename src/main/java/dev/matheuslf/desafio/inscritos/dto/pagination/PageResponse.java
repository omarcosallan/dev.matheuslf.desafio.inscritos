package dev.matheuslf.desafio.inscritos.dto.pagination;

import java.util.List;

public record PageResponse<T>(
        List<T> data,
        Integer currentPage,
        Integer totalPages, Long totalItems,
        Integer pageSize,
        Boolean hasNext,
        Boolean hasPrevious
) {
}
