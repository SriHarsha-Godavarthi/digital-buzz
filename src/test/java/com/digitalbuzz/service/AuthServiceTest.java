package com.digitalbuzz.service;

import com.digitalbuzz.dto.request.LoginRequest;
import com.digitalbuzz.dto.request.RegisterRequest;
import com.digitalbuzz.dto.response.AuthResponse;
import com.digitalbuzz.exception.DuplicateResourceException;
import com.digitalbuzz.exception.UnauthorizedException;
import com.digitalbuzz.model.User;
import com.digitalbuzz.model.enums.Role;
import com.digitalbuzz.model.enums.UserStatus;
import com.digitalbuzz.repository.UserRepository;
import com.digitalbuzz.util.EntityMapper;
import com.digitalbuzz.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .build();

        user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("encoded_password")
                .role(Role.READER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Test
    void testRegisterSuccess() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(any())).thenReturn("jwt_token");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt_token");
        assertThat(response.getType()).isEqualTo("Bearer");
    }

    @Test
    void testRegisterDuplicateUsername() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("username");
    }

    @Test
    void testRegisterDuplicateEmail() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");
    }

    @Test
    void testLoginInactiveUser() {
        User inactiveUser = User.builder()
                .username("testuser")
                .password("encoded_password")
                .status(UserStatus.INACTIVE)
                .build();

        LoginRequest loginRequest = LoginRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(inactiveUser));

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("not active");
    }

    @Test
    void testLoginUserNotFound() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("nonexistent")
                .password("password123")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(UnauthorizedException.class);
    }
}
