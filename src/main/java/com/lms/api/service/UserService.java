package com.lms.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.api.dto.UserResponseDto;
import com.lms.api.dto.UserRequestDto;
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

    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRoles().stream().map(Role::getName).toList()));
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRoles().stream().map(Role::getName).toList()))
                .toList();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRequestDto createUser(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }

        User user = new User(dto.getName(), dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        String roleName = dto.getRole();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + roleName));

        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);
        return new UserRequestDto(savedUser.getName(), savedUser.getEmail(), "", roleName, "");
    }

    public UserRequestDto updateUser(Long id, UserRequestDto dto) {
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Password does not match for user " + user.getEmail());
        }

        String roleName = dto.getRole();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + roleName));
        
        user.setRoles(new HashSet<>(List.of(role)));

        if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        User updatedUser = userRepository.save(user);
        return new UserRequestDto(updatedUser.getName(), updatedUser.getEmail(), "",
                roleName, "");
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
        userRepository.delete(user);
    }

}