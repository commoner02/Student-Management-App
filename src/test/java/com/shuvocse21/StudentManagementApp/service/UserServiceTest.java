package com.shuvocse21.StudentManagementApp.service;

import com.shuvocse21.StudentManagementApp.entity.*;
import com.shuvocse21.StudentManagementApp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;

    private User testUser;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setRole("STUDENT");

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setUser(testUser);
        testStudent.setStudentId("S1001");
    }

    @Test
    void registerStudent() {
        when(userRepository.existsByUsername("newstudent")).thenReturn(false);
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(studentRepository.existsByStudentId("S1002")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        User result = userService.registerStudent("newstudent", "pass", "new@test.com",
                "S1002", "555-1234", "123 St");

        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void registerStudentDuplicateUsername() {
        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThatThrownBy(() ->
                userService.registerStudent("existing", "pass", "email", "SID", "phone", "addr"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void registerTeacher() {
        when(userRepository.existsByUsername("newteacher")).thenReturn(false);
        when(userRepository.existsByEmail("teacher@test.com")).thenReturn(false);
        when(teacherRepository.existsByEmployeeId("T1002")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.registerTeacher("newteacher", "pass", "teacher@test.com", "T1002");

        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void enrollStudentInCourse() {
        Course testCourse = new Course();
        testCourse.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        userService.enrollStudentInCourse(1L, 1L);

        verify(studentRepository).save(any(Student.class));
        assertThat(testStudent.getCourses()).contains(testCourse);
    }

    @Test
    void enrollStudentInCourse_CourseNotFound_ShouldThrowException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                userService.enrollStudentInCourse(1L, 999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void enrollStudentInCourse_StudentNotFound_ShouldThrowException() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                userService.enrollStudentInCourse(999L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void registerStudent_WithEmptyFields_ShouldFail() {

        assertThatThrownBy(() ->
                userService.registerStudent("", "", "", "", "", ""))
                .isInstanceOf(Exception.class);
    }

    // NON-ESSENTIAL METHODS (Blank)
    @Test
    void getUserByUsername() { }

    @Test
    void getStudentByUserId() { }

    @Test
    void getStudentDTOByUserId() { }

    @Test
    void getAllStudentDTOs() { }

    @Test
    void getAllCourseDTOs() { }

    @Test
    void updateStudent() { }

    @Test
    void deleteStudent() { }

    @Test
    void removeStudentFromCourse() { }

    @Test
    void getStudentById() { }

    @Test
    void getCourseById() { }

    @Test
    void getAllCourses() { }
}