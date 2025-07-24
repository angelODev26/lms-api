package com.lms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be defined here if needed

    
}
