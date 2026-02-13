package com.shuvocse21.StudentManagementApp.repository;

import com.shuvocse21.StudentManagementApp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        testUser.setRole("STUDENT");
        testUser.setEnabled(true);
        entityManager.persistAndFlush(testUser);
    }

    @Test
    void testFindByUsername() {
        Optional<User> found = userRepository.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testExistsByUsername() {
        Boolean exists = userRepository.existsByUsername("testuser");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmail() {
        Boolean exists = userRepository.existsByEmail("test@example.com");
        assertThat(exists).isTrue();
    }
}