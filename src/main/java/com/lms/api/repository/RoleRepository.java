package com.lms.api.repository;

import com.lms.api.model.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    // Additional query methods can be defined here if needed

    Optional<Role> findByName(String name);
}
