package com.test.expensetrackerapi.repository;

import com.test.expensetrackerapi.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{'_id': ?0,'objStatus': 'ACTIVE'}")
    Optional<User> findActiveById(String id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
