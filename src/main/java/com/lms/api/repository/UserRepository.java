package com.lms.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be defined here if needed

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
    
    Optional<User> findByEmail(String email);
}
