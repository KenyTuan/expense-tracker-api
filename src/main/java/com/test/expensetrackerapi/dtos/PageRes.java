package com.test.expensetrackerapi.dtos;

import java.io.Serializable;
import java.util.Set;

public record PageRes<T>(
        Set<T> content,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) implements Serializable {
}
