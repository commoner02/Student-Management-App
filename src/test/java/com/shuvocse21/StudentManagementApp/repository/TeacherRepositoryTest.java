package com.shuvocse21.StudentManagementApp.repository;

import com.shuvocse21.StudentManagementApp.entity.Teacher;
import com.shuvocse21.StudentManagementApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;

    // ESSENTIAL METHOD
    @Test
    void findByUserId() {
        User user = new User();
        user.setUsername("drjones");
        user.setPassword("pass");
        user.setEmail("jones@school.com");
        user.setRole("TEACHER");
        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setEmployeeId("T1002");
        teacherRepository.save(teacher);

        Teacher found = teacherRepository.findByUserId(savedUser.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getEmployeeId()).isEqualTo("T1002");
    }

    // NON-ESSENTIAL METHODS (Blank)
    @Test
    void save() { }

    @Test
    void existsByEmployeeId() { }
}