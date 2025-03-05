package com.test.expensetrackerapi.converter;

import com.test.expensetrackerapi.dtos.expense.ExpenseReq;
import com.test.expensetrackerapi.dtos.expense.ExpenseRes;
import com.test.expensetrackerapi.model.entity.Expense;
import com.test.expensetrackerapi.model.enums.ObjStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpenseConverter {

    /**
     * Converts a ExpenseReq to an Expense.
     *
     * @param req The ExpenseReq to convert.
     * @return The converted Expense.
     */
    public static Expense convertToEntity(ExpenseReq req) {
        final Expense expense = Expense
                .builder()
                .description(req.getDescription())
                .amount(req.getAmount())
                .category(req.getCategory())
                .userId(req.getUserId())
                .dateTime(LocalDateTime.now())
                .build();
        expense.setObjStatus(ObjStatus.ACTIVE);
        return expense;
    }

    /**
     * Converts a ExpenseReq to an Expense.
     *
     * @param req The ExpenseReq to convert.
     * @param oldExpense The Expense to convert.
     * @return The converted Expense.
     */
    public static Expense convertToEntity(ExpenseReq req, Expense oldExpense) {
        final Expense expense = Expense
                .builder()
                .description(req.getDescription())
                .amount(req.getAmount())
                .category(req.getCategory())
                .userId(req.getUserId())
                .dateTime(oldExpense.getDateTime())
                .build();
        expense.setObjStatus(ObjStatus.ACTIVE);
        return expense;
    }

    /**
     * Converts an Expense to a ExpenseRes.
     *
     * @param expense The Expense to convert.
     * @return The converted ExpenseRes.
     */
    public static ExpenseRes convertToDto(Expense expense) {
        return new ExpenseRes(
                expense.getId(),
                expense.getUserId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDateTime(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }

    /**
     * Converts a list of Expense objects to a list of ExpenseRes objects.
     *
     * @param expenses The list of Expense objects to convert.
     * @return The list of converted ExpenseRes objects.
     */
    public static Set<ExpenseRes> convertToDtoList(Set<Expense> expenses) {
        if (expenses == null) {
            return new HashSet<>();
        }

        return expenses.stream()
                .map(ExpenseConverter::convertToDto)
                .collect(Collectors.toSet());
    }

    /**
     * Converts a list of ExpenseReq objects to a list of Expense objects.
     *
     * @param reqs The list of ExpenseReq objects to convert.
     * @return The list of converted Expense objects.
     */
    public Set<Expense> convertToEntitySet(Set<ExpenseReq> reqs) {
        return reqs.stream()
                .map(ExpenseConverter::convertToEntity)
                .collect(Collectors.toSet());
    }
}
