package com.lms.api.dto;

import java.util.List;

public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private List<String> role;

    public UserResponseDto() {}

    public UserResponseDto(Long id, String name, String email, List<String> role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return role;
    }

    public void setRoles(List<String> role) {
        this.role = role;
    }
    
}
