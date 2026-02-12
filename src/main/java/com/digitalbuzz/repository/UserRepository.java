package com.digitalbuzz.repository;

import com.digitalbuzz.model.User;
import com.digitalbuzz.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameAndStatus(String username, UserStatus status);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
