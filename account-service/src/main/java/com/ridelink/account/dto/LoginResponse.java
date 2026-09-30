package com.ridelink.account.dto;

import com.ridelink.account.entity.Role;

public class LoginResponse {

    private String token;
    private Role role;
    private Long accountId;
    private String email;
    private String name;

    public LoginResponse() {
    }

    public LoginResponse(String token, Role role, Long accountId, String email, String name) {
        this.token = token;
        this.role = role;
        this.accountId = accountId;
        this.email = email;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
