package com.digitalbuzz.repository;

import com.digitalbuzz.model.User;
import com.digitalbuzz.model.enums.Role;
import com.digitalbuzz.model.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(Role.READER)
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByUsername() {
        User user = User.builder()
                .username("john")
                .email("john@example.com")
                .password("password")
                .role(Role.AUTHOR)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        User foundUser = userRepository.findByUsername("john").orElse(null);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("john");
    }

    @Test
    void testExistsByUsername() {
        User user = User.builder()
                .username("jane")
                .email("jane@example.com")
                .password("password")
                .role(Role.EDITOR)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("jane");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmail() {
        User user = User.builder()
                .username("bob")
                .email("bob@example.com")
                .password("password")
                .role(Role.READER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("bob@example.com");

        assertThat(exists).isTrue();
    }
}
