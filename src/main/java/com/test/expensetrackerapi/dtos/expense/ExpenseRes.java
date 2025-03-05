package com.test.expensetrackerapi.dtos.expense;

import com.test.expensetrackerapi.model.enums.Category;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ExpenseRes(
        String id,
        String userId,
        String description,
        double amount,
        Category category,
        LocalDateTime dateTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}
