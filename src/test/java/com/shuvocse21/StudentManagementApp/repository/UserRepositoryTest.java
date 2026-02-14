package com.shuvocse21.StudentManagementApp.repository;

import com.shuvocse21.StudentManagementApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // ESSENTIAL METHOD
    @Test
    void findByUsername() {
        User user = new User();
        user.setUsername("jane");
        user.setPassword("pass");
        user.setEmail("jane@test.com");
        user.setRole("STUDENT");
        userRepository.save(user);

        User found = userRepository.findByUsername("jane").orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("jane@test.com");
    }

    // NON-ESSENTIAL METHODS (Blank)
    @Test
    void save() { }

    @Test
    void existsByUsername() { }

    @Test
    void existsByEmail() { }
}