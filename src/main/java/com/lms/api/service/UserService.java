package com.lms.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.api.dto.UserDto;
import com.lms.api.model.Role;
import com.lms.api.model.User;
import com.lms.api.repository.RoleRepository;
import com.lms.api.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDto(user.getName(), user.getEmail()));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDto(user.getName(), user.getEmail()))
                .toList();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto createUser(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }

        User user = new User(dto.getName(), dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        Role defaultRole = roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new NoSuchElementException("Role not found: ROLE_USER"));
        
        user.setRoles(Set.of(defaultRole));
        
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser.getName(), savedUser.getEmail());
    }

    public UserDto updateUser(Long id, UserDto dto) {
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        User updatedUser = userRepository.save(user);
        return new UserDto(updatedUser.getName(), updatedUser.getEmail());
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
        userRepository.delete(user);
    }

}