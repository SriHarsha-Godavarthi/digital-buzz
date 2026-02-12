package com.digitalbuzz.service;

import com.digitalbuzz.dto.request.UpdateRoleRequest;
import com.digitalbuzz.dto.request.UpdateUserRequest;
import com.digitalbuzz.dto.response.UserResponse;
import com.digitalbuzz.exception.ResourceNotFoundException;
import com.digitalbuzz.model.User;
import com.digitalbuzz.repository.UserRepository;
import com.digitalbuzz.util.EntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;
    
    public UserService(UserRepository userRepository, EntityMapper entityMapper) {
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
    }
    
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String username) {
        logger.debug("Getting current user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return entityMapper.toUserResponse(user);
    }
    
    @Transactional
    public UserResponse updateCurrentUser(String username, UpdateUserRequest request) {
        logger.info("Updating user profile: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }
        
        user = userRepository.save(user);
        logger.info("User profile updated: {}", username);
        
        return entityMapper.toUserResponse(user);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        logger.debug("Getting all users");
        return userRepository.findAll().stream()
                .map(entityMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public UserResponse updateUserRole(Long userId, UpdateRoleRequest request) {
        logger.info("Updating role for user ID: {} to {}", userId, request.getRole());
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setRole(request.getRole());
        user = userRepository.save(user);
        
        logger.info("User role updated: {} - {}", user.getUsername(), user.getRole());
        return entityMapper.toUserResponse(user);
    }
}
