package com.test.expensetrackerapi.service.impl;

import com.test.expensetrackerapi.converter.ExpenseConverter;
import com.test.expensetrackerapi.dtos.PageRes;
import com.test.expensetrackerapi.dtos.expense.ExpenseReq;
import com.test.expensetrackerapi.dtos.expense.ExpenseRes;
import com.test.expensetrackerapi.exception.BadRequestException;
import com.test.expensetrackerapi.exception.ErrorCode;
import com.test.expensetrackerapi.exception.NotFoundException;
import com.test.expensetrackerapi.model.entity.Expense;
import com.test.expensetrackerapi.model.entity.User;
import com.test.expensetrackerapi.model.enums.ObjStatus;
import com.test.expensetrackerapi.repository.ExpenseRepository;
import com.test.expensetrackerapi.repository.UserRepository;
import com.test.expensetrackerapi.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    @Override
    public Set<ExpenseRes> getAllExpense() {
        log.info("Get all expense records");
        return ExpenseConverter
                .convertToDtoList(expenseRepository.findAllActive());
    }

    @Override
    @Cacheable(value = "expenses", key = "#id")
    public ExpenseRes getExpenseById(String id) {
        return ExpenseConverter
                .convertToDto(findExpenseById(id));
    }

    @Override
    @Cacheable(value = "expenses", key = "#userId")
    public Set<ExpenseRes> getAllExpenseByUserId(String userId) {
        log.info("Get all expense records by user id");
        return ExpenseConverter
                .convertToDtoList(expenseRepository.findAllActiveByUserId(userId));
    }

    @Override
    @Cacheable(value = "expenses", key = "#id + '_' + #userId")
    public ExpenseRes getExpenseByIdAndUserId(String id, String userId) {
        return ExpenseConverter.convertToDto(
                findExpenseByIdAndUserId(id, userId));
    }

    @Override
    @Cacheable(value = "expenses", key = "#desc + " +
            "'-' + #from + '-' + #to + '-' + " +
            "#page + '-' + #size + '-' + #sortBy + '-' + #sortDir")
    public PageRes<ExpenseRes> searchAndFilterExpenses(
            String desc, LocalDateTime to,
            LocalDateTime from, int page,
            int size, String sortBy,
            String sortDir) {

        from = from == null ? LocalDateTime.now() : from;
        to = to == null ? LocalDateTime.now() : to;

        if (to.isBefore(from)) {
            throw new BadRequestException("PCK-0024","'to' date must be after 'from' date");
        }

        final Page<Expense> expenses = expenseRepository
                .findActiveByDescriptionContainingAndDateTimeBetween(
                        desc, from, to,
                        setupPageRequest(page, size, sortBy, sortDir));

        final Set<ExpenseRes> expenseRes = expenses
                .stream()
                .map(ExpenseConverter::convertToDto)
                .collect(Collectors.toSet());
        return new PageRes<>(
                expenseRes,
                expenses.getNumber(),
                expenses.getSize(),
                expenses.getTotalElements(),
                expenses.getTotalPages(),
                expenses.isLast()
        );
    }

    @Override
    @Cacheable(value = "expenses", key = "#desc + " +
            "'-' + #userId + '-' + #from + '-' + #to + '-' + " +
            "#page + '-' + #size + '-' + #sortBy + '-' + #sortDir")
    public PageRes<ExpenseRes> searchAndFilterExpensesUser(
            String desc, LocalDateTime to,
            LocalDateTime from, String userId,
            int page, int size,
            String sortBy, String sortDir) {
        final Page<Expense> expenses = expenseRepository
                .findActiveByDescriptionContainingAndDateTimeBetweenAndUserId(
                        desc, userId, from, to,
                        setupPageRequest(page, size, sortBy, sortDir));

        final Set<ExpenseRes> expenseRes = expenses
                .stream()
                .map(ExpenseConverter::convertToDto)
                .collect(Collectors.toSet());

        return new PageRes<>(
                expenseRes,
                expenses.getNumber(),
                expenses.getSize(),
                expenses.getTotalElements(),
                expenses.getTotalPages(),
                expenses.isLast()
        );
    }

    @Override
    @Transactional
    public ExpenseRes addExpense(ExpenseReq expenseReq) {
        log.info("Starting to add expense for user with ID: {}", expenseReq.getUserId());
        final Expense expense = ExpenseConverter.convertToEntity(expenseReq);

        log.debug("Converted ExpenseReq to Expense entity: {}", expense);
        final User user = findUserById(expenseReq.getUserId());

        log.debug("Found user: {}", user);
        final Expense addExp = expenseRepository.save(expense);
        log.info("Expense saved successfully with ID: {}", addExp.getId());

        user.getExpenses().add(addExp);
        log.debug("Added expense to user's expense list");

        userRepository.save(user);
        log.info("User updated successfully with new expense");

        return ExpenseConverter.convertToDto(addExp);
    }

    @Override
    @Transactional
    @CachePut(value = "expenses", key = "#id")
    public ExpenseRes updateExpense(ExpenseReq expenseReq, String id) {
        final Expense expense = findExpenseById(id);
        final User user = findUserById(expenseReq.getUserId());
        final Expense updatedExp = expenseRepository
                .save(ExpenseConverter
                        .convertToEntity(expenseReq, expense));

        softDeleteExpense(expense);

        user.getExpenses().remove(expense);
        user.getExpenses().add(updatedExp);
        userRepository.save(user);

        return ExpenseConverter.convertToDto(updatedExp);
    }

    @Override
    @Transactional
    @CacheEvict(value = "expenses", key = "#id")
    public void deleteExpenseById(String id) {
        final Expense expense = findExpenseById(id);
        final User user = findUserById(expense.getUserId());

        softDeleteExpense(expense);
        user.getExpenses().remove(expense);
        userRepository.save(user);
    }

    private void softDeleteExpense(Expense expense) {
        expense.setObjStatus(ObjStatus.DELETED);
        expenseRepository.save(expense);
    }

    private Expense findExpenseById(String id) {
        log.info("find expense by id {}", id);
        return expenseRepository
                .findActiveById(id).orElseThrow(()-> {
                    log.warn("expense not found with id {}", id);
                    return new NotFoundException(ErrorCode.EXPENSE_NOT_FOUND.getErrMessage());
                });
    }

    private Expense findExpenseByIdAndUserId(String id, String userId) {
        log.info("Fetching expense with id: {} and userId: {}", id, userId);
        return expenseRepository.findActiveByIdAndUserId(id,userId)
                .orElseThrow(()->{
                    log.warn("Expense not found with id: {} and userId: {}", id, userId);
                    return new NotFoundException(ErrorCode.EXPENSE_NOT_FOUND.getErrMessage());
                });
    }

    private User findUserById(String id) {
        log.info("find user by id {}", id);
        return  userRepository.findActiveById(id).orElseThrow(()-> {
            log.warn("user not found with id {}", id);
            return new NotFoundException(ErrorCode.USER_NOT_FOUND.getErrMessage());
        });
    }

    private PageRequest setupPageRequest(
            int page, int size,
            String sortBy, String sortDir) {

        final Sort sort = sortDir.equalsIgnoreCase(
                Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }
}
