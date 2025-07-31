package com.lms.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.api.dto.UserRequestDto;
import com.lms.api.dto.UserResponseDto;
import com.lms.api.payload.ApiResponse;
import com.lms.api.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        ApiResponse<List<UserResponseDto>> response = new ApiResponse<>(true, "Users retrieved successfully", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        Optional<UserResponseDto> userDto = userService.getUserById(id);

        if (userDto.isPresent()) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(true, "User found", userDto.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(false, "User whith id " + id + " not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<UserRequestDto> createUser(@Valid @RequestBody UserRequestDto userDto) {
        UserRequestDto createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRequestDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto userDto) {
        UserRequestDto updatedUser = userService.updateUser(id, userDto);
        ApiResponse<UserRequestDto> response = new ApiResponse<>(true, "User updated successfully", updatedUser);
        return ResponseEntity.ok(response);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponse<String> response = new ApiResponse<>(true, "User deleted successfully", null);
        return ResponseEntity.ok(response); 
    }
}
