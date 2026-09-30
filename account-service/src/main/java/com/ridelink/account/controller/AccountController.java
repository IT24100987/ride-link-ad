package com.ridelink.account.controller;

import com.ridelink.account.dto.*;
import com.ridelink.account.entity.Role;
import com.ridelink.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "Endpoints for user registration, authentication, profiles and roles")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new passenger, driver, or admin account")
    public ResponseEntity<AccountResponse> register(@Valid @RequestBody RegisterRequest request) {
        AccountResponse response = accountService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate with email and password to receive a JWT Bearer token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = accountService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve account details by account ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account name and phone details")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request) {
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update account status (ACTIVE, INACTIVE, BLOCKED) - Admin only")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AccountResponse> updateStatus(@PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        AccountResponse response = accountService.updateAccountStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/role")
    @Operation(summary = "Retrieve account role by account ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        Role role = accountService.getAccountRole(id);
        return ResponseEntity.ok(role);
    }
}
