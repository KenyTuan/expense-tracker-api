package com.test.expensetrackerapi.dtos.user;

import com.test.expensetrackerapi.dtos.expense.ExpenseRes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public record UserRes(
        String id,
        String email,
        String name,
        Set<ExpenseRes>  expenses,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}
