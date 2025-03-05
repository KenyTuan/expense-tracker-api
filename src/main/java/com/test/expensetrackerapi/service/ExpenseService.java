package com.test.expensetrackerapi.service;

import com.test.expensetrackerapi.dtos.PageRes;
import com.test.expensetrackerapi.dtos.expense.ExpenseReq;
import com.test.expensetrackerapi.dtos.expense.ExpenseRes;

import java.time.LocalDateTime;
import java.util.Set;

public interface ExpenseService {

    Set<ExpenseRes> getAllExpense();

    ExpenseRes getExpenseById(String id);

    Set<ExpenseRes> getAllExpenseByUserId(String userId);

    ExpenseRes getExpenseByIdAndUserId(String id, String userId);

    PageRes<ExpenseRes> searchAndFilterExpenses(String desc, LocalDateTime to,
                                                LocalDateTime from,
                                             int page, int size,
                                             String sortBy, String sortDir);

    PageRes<ExpenseRes> searchAndFilterExpensesUser(String desc, LocalDateTime to,
                                                 LocalDateTime from, String userId,
                                                 int page, int size,
                                                 String sortBy, String sortDir);

    ExpenseRes addExpense(ExpenseReq expenseReq);

    ExpenseRes updateExpense(ExpenseReq expenseReq, String id);

    void deleteExpenseById(String id);
}
