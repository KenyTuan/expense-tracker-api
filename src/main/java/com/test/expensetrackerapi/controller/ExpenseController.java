package com.test.expensetrackerapi.controller;

import com.test.expensetrackerapi.constants.APIEndPoint;
import com.test.expensetrackerapi.dtos.PageRes;
import com.test.expensetrackerapi.dtos.expense.ExpenseReq;
import com.test.expensetrackerapi.dtos.expense.ExpenseRes;
import com.test.expensetrackerapi.service.ExpenseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIEndPoint.PREFIX)
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping(APIEndPoint.EXPENSE_V1)
    public Set<ExpenseRes> getAllExpense() {
        return expenseService.getAllExpense();
    }

    @GetMapping(APIEndPoint.EXPENSE_V1 + "/{id}")
    public ExpenseRes getExpenseById(@PathVariable String id) {
        return expenseService.getExpenseById(id);
    }

    @GetMapping(APIEndPoint.EXPENSE_V1 + "/user/{userId}")
    public  Set<ExpenseRes> getExpenseByUserId(@PathVariable String userId) {
        return expenseService.getAllExpenseByUserId(userId);
    }

    @GetMapping(APIEndPoint.EXPENSE_V1 + "/{id}/user/{userId}")
    public ExpenseRes getExpenseByIdAndUserId(
            @PathVariable String id,
            @PathVariable String userId) {
        return expenseService.getExpenseByIdAndUserId(id, userId);
    }

    @GetMapping(APIEndPoint.EXPENSE_V1 + "/search")
    public PageRes<ExpenseRes> searchAndFilterExpenses(
            @RequestParam(required = false) String desc,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc")
            @Pattern(regexp = "asc|desc",
                    message = "Sort direction must be 'asc' or 'desc'")
            String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ){
        return expenseService.searchAndFilterExpenses(desc, to, from, page, size, sortBy, sortDir);
    }

    @GetMapping(APIEndPoint.EXPENSE_V1 + "/user/{userId}/search")
    public PageRes<ExpenseRes> searchAndFilterExpenses(
            @PathVariable String userId,
            @RequestParam String desc,
            @RequestParam LocalDateTime to,
            @RequestParam LocalDateTime from,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortBy,
            @RequestParam(defaultValue = "title") String sortDir
    ){
        return expenseService.searchAndFilterExpensesUser(desc, to, from, userId,
                page, size, sortBy, sortDir);
    }

    @PostMapping(APIEndPoint.EXPENSE_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseRes addExpense(@RequestBody @Valid ExpenseReq req) {
        return expenseService.addExpense(req);
    }

    @PutMapping(APIEndPoint.EXPENSE_V1 + "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExpenseRes updateExpense(@RequestBody @Valid ExpenseReq req,
                                    @PathVariable String id) {
        return expenseService.updateExpense(req, id);
    }

    @DeleteMapping(APIEndPoint.EXPENSE_V1 + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpenseById(@PathVariable String id) {
        expenseService.deleteExpenseById(id);
    }
}
