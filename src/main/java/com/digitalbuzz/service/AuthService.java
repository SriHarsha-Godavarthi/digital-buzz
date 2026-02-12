package com.digitalbuzz.service;

import com.digitalbuzz.dto.request.LoginRequest;
import com.digitalbuzz.dto.request.RegisterRequest;
import com.digitalbuzz.dto.response.AuthResponse;
import com.digitalbuzz.dto.response.UserResponse;
import com.digitalbuzz.exception.DuplicateResourceException;
import com.digitalbuzz.exception.UnauthorizedException;
import com.digitalbuzz.model.User;
import com.digitalbuzz.model.enums.Role;
import com.digitalbuzz.model.enums.UserStatus;
import com.digitalbuzz.repository.UserRepository;
import com.digitalbuzz.util.EntityMapper;
import com.digitalbuzz.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final EntityMapper entityMapper;
    
    public AuthService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtUtil jwtUtil,
                      AuthenticationManager authenticationManager,
                      UserDetailsServiceImpl userDetailsService,
                      EntityMapper entityMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.entityMapper = entityMapper;
    }
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering new user: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.READER)
                .status(UserStatus.ACTIVE)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        
        user = userRepository.save(user);
        logger.info("User registered successfully: {}", user.getUsername());
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        
        UserResponse userResponse = entityMapper.toUserResponse(user);
        
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .user(userResponse)
                .build();
    }
    
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        logger.info("User login attempt: {}", request.getUsername());
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
        
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("User account is not active");
        }
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        
        UserResponse userResponse = entityMapper.toUserResponse(user);
        
        logger.info("User logged in successfully: {}", user.getUsername());
        
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .user(userResponse)
                .build();
    }
}
