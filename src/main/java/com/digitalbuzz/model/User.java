package com.digitalbuzz.model;

import com.digitalbuzz.model.enums.Role;
import com.digitalbuzz.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       },
       indexes = {
           @Index(name = "idx_username", columnList = "username"),
           @Index(name = "idx_email", columnList = "email")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;
    
    @Column(length = 100)
    private String firstName;
    
    @Column(length = 100)
    private String lastName;
    
    @Column(length = 500)
    private String bio;
    
    @Column(length = 255)
    private String profileImage;
}
