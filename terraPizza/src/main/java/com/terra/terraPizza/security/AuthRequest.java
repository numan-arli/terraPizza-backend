package com.terra.terraPizza.security;

public class AuthRequest {
    private String email;
    private String password;
    // Getters and setters

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
}
