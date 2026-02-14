package com.shuvocse21.StudentManagementApp.repository;

import com.shuvocse21.StudentManagementApp.entity.Student;
import com.shuvocse21.StudentManagementApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    // ESSENTIAL METHOD
    @Test
    void findByStudentId() {
        User user = new User();
        user.setUsername("studentjane");
        user.setPassword("pass123");
        user.setEmail("jane@student.com");
        user.setRole("STUDENT");
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        student.setStudentId("S2001");
        student.setPhone("555-0101");
        student.setAddress("123 Campus Drive");
        studentRepository.save(student);

        Optional<Student> found = studentRepository.findByStudentId("S2001");

        assertThat(found).isPresent();
        assertThat(found.get().getStudentId()).isEqualTo("S2001");
    }

    // NON-ESSENTIAL METHODS (Blank)
    @Test
    void findByUserId() { }

    @Test
    void existsByStudentId() { }
}