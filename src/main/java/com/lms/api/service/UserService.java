package com.lms.api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lms.api.dto.UserDto;
import com.lms.api.model.User;
import com.lms.api.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public UserDto createUser(UserDto dto) {
        User user = new User(dto.getName(), dto.getEmail());
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser.getName(), savedUser.getEmail());
    }

    public UserDto updateUser(Long id, UserDto dto) {
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