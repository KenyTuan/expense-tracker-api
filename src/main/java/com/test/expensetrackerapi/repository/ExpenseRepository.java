package com.test.expensetrackerapi.repository;

import com.test.expensetrackerapi.model.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {

    @Query("{'objStatus': 'ACTIVE'}")
    Set<Expense> findAllActive();

    @Query("{'_id': ?0,'objStatus': 'ACTIVE'}")
    Optional<Expense> findActiveById(String id);

    @Query("{'userId': ?0,'objStatus': 'ACTIVE'}")
    Set<Expense> findAllActiveByUserId(String userId);

    @Query("{'_id': ?0,'userId': ?1,'objStatus': 'ACTIVE'}")
    Optional<Expense> findActiveByIdAndUserId(String id, String userId);

    @Query("{ 'description': { $regex: ?0, $options: 'i' }," +
            "'dateTime': { $gte: ?1, $lte: ?2 }," +
            "'objStatus': 'ACTIVE' }")
    Page<Expense> findActiveByDescriptionContainingAndDateTimeBetween(
            String desc, LocalDateTime from, LocalDateTime to, PageRequest pageRequest);

    @Query("{ 'description': { $regex: ?0, $options: 'i' }," +
            "'userId': ?1,'dateTime': { $gte: ?2, $lte: ?3 }," +
            "'objStatus': 'ACTIVE' }")
    Page<Expense> findActiveByDescriptionContainingAndDateTimeBetweenAndUserId(
            String desc, String userId,
            LocalDateTime from, LocalDateTime to,
            PageRequest pageRequest);
}
