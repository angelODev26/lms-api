package com.lms.api.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lms.api.model.Role;
import com.lms.api.repository.RoleRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR");
        for (String roleName : roles){
            roleRepository.findByName(roleName)
            .orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
    }

    
}
