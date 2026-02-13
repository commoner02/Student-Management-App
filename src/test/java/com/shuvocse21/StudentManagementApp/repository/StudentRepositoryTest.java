package com.shuvocse21.StudentManagementApp.repository;

import com.shuvocse21.StudentManagementApp.entity.Student;
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
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("studentuser");
        testUser.setPassword("password");
        testUser.setEmail("student@example.com");
        testUser.setRole("STUDENT");
        testUser.setEnabled(true);
        entityManager.persist(testUser);

        testStudent = new Student();
        testStudent.setUser(testUser);
        testStudent.setStudentId("S12345");
        testStudent.setPhone("1234567890");
        testStudent.setAddress("Test Address");
        entityManager.persistAndFlush(testStudent);
    }

    @Test
    void testFindByStudentId() {
        Optional<Student> found = studentRepository.findByStudentId("S12345");
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUsername()).isEqualTo("studentuser");
    }

    @Test
    void testFindByUserId() {
        Optional<Student> found = studentRepository.findByUserId(testUser.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStudentId()).isEqualTo("S12345");
    }

    @Test
    void testExistsByStudentId() {
        Boolean exists = studentRepository.existsByStudentId("S12345");
        assertThat(exists).isTrue();
    }
}