package com.ridelink.account.service;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.LoginResponse;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.entity.Account;
import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.entity.Role;
import com.ridelink.account.exception.EmailAlreadyExistsException;
import com.ridelink.account.exception.InvalidCredentialsException;
import com.ridelink.account.exception.ResourceNotFoundException;
import com.ridelink.account.repository.AccountRepository;
import com.ridelink.account.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccountService accountService;

    private Account sampleAccount;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        sampleAccount = new Account(
                1L,
                "Kamal",
                "Perera",
                "kamal@example.com",
                "encodedPassword123",
                "0771234567",
                Role.PASSENGER,
                AccountStatus.ACTIVE);

        registerRequest = new RegisterRequest(
                "Kamal",
                "Perera",
                "kamal@example.com",
                "Password123",
                "0771234567",
                Role.PASSENGER);
    }

    @Test
    @DisplayName("Should successfully register a new account with BCrypt encoded password")
    @SuppressWarnings("null")
    void register_Success() {
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("Password123")).thenReturn("encodedPassword123");
        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);

        AccountResponse response = accountService.register(registerRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("kamal@example.com", response.getEmail());
        assertEquals(Role.PASSENGER, response.getRole());
        assertEquals(AccountStatus.ACTIVE, response.getStatus());

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when registering existing email")
    @SuppressWarnings("null")
    void register_DuplicateEmail_ThrowsException() {
        when(accountRepository.existsByEmail("kamal@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> accountService.register(registerRequest));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should successfully login and return JWT token")
    void login_Success() {
        LoginRequest loginRequest = new LoginRequest("kamal@example.com", "Password123");
        when(accountRepository.findByEmail("kamal@example.com")).thenReturn(Optional.of(sampleAccount));
        when(passwordEncoder.matches("Password123", "encodedPassword123")).thenReturn(true);
        when(jwtService.generateToken(1L, "kamal@example.com", Role.PASSENGER, "Kamal Perera"))
                .thenReturn("mocked.jwt.token");

        LoginResponse response = accountService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getToken());
        assertEquals(Role.PASSENGER, response.getRole());
        assertEquals(1L, response.getAccountId());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException on wrong password")
    void login_WrongPassword_ThrowsException() {
        LoginRequest loginRequest = new LoginRequest("kamal@example.com", "WrongPassword");
        when(accountRepository.findByEmail("kamal@example.com")).thenReturn(Optional.of(sampleAccount));
        when(passwordEncoder.matches("WrongPassword", "encodedPassword123")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> accountService.login(loginRequest));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when account ID does not exist")
    void getAccountById_NotFound_ThrowsException() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(99L));
    }
}
