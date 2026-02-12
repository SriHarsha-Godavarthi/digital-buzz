package com.digitalbuzz.dto.response;

import com.digitalbuzz.model.enums.Role;
import com.digitalbuzz.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private UserStatus status;
    private String firstName;
    private String lastName;
    private String bio;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
