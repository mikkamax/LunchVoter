package com.mike.lunchvoter.repository;

import com.mike.lunchvoter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    /**
     * Checks if object with same email (and DIFFERENT id) already exists in database
     *
     * @param userId of object to save
     * @param email  of object to save
     * @return true if such object exists in the database, false otherwise
     */
    boolean existsByIdNotAndEmailEqualsIgnoreCase(Integer userId, String email);

}
