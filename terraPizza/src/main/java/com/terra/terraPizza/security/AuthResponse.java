package com.terra.terraPizza.security;

import com.terra.terraPizza.Entities.Role;

public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AuthResponse(String token, String email, String name, Role role) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
// Getter

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
