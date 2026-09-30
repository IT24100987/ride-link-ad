package com.ridelink.account.service;

import com.ridelink.account.dto.*;
import com.ridelink.account.entity.Account;
import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.entity.Role;
import com.ridelink.account.exception.EmailAlreadyExistsException;
import com.ridelink.account.exception.InvalidCredentialsException;
import com.ridelink.account.exception.ResourceNotFoundException;
import com.ridelink.account.repository.AccountRepository;
import com.ridelink.account.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AccountService(AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AccountResponse register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new EmailAlreadyExistsException("Email '" + request.getEmail() + "' is already registered.");
        }

        Account account = new Account();
        account.setFirstName(request.getFirstName().trim());
        account.setLastName(request.getLastName().trim());
        account.setEmail(request.getEmail().toLowerCase().trim());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setPhone(request.getPhone().trim());
        account.setRole(request.getRole());
        account.setStatus(AccountStatus.ACTIVE);

        Account saved = accountRepository.save(account);
        return new AccountResponse(saved);
    }

    public LoginResponse login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidCredentialsException(
                    "Account is currently " + account.getStatus() + ". Please contact support.");
        }

        String fullName = account.getFirstName() + " " + account.getLastName();
        String token = jwtService.generateToken(account.getId(), account.getEmail(), account.getRole(), fullName);

        return new LoginResponse(token, account.getRole(), account.getId(), account.getEmail(), fullName);
    }

    public AccountResponse getAccountById(Long id) {
        Long accountId = Objects.requireNonNull(id, "id must not be null");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        return new AccountResponse(account);
    }

    @Transactional
    public AccountResponse updateAccount(Long id, UpdateAccountRequest request) {
        Long accountId = Objects.requireNonNull(id, "id must not be null");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        account.setFirstName(request.getFirstName().trim());
        account.setLastName(request.getLastName().trim());
        account.setPhone(request.getPhone().trim());

        Account updated = accountRepository.save(account);
        return new AccountResponse(updated);
    }

    @Transactional
    public AccountResponse updateAccountStatus(Long id, UpdateStatusRequest request) {
        Long accountId = Objects.requireNonNull(id, "id must not be null");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        account.setStatus(request.getStatus());
        Account updated = accountRepository.save(account);
        return new AccountResponse(updated);
    }

    public Role getAccountRole(Long id) {
        Long accountId = Objects.requireNonNull(id, "id must not be null");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        return account.getRole();
    }
}
