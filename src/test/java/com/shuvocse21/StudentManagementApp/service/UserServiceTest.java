package com.shuvocse21.StudentManagementApp.service;

import com.shuvocse21.StudentManagementApp.dto.StudentDTO;
import com.shuvocse21.StudentManagementApp.entity.*;
import com.shuvocse21.StudentManagementApp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Student testStudent;
    private Teacher testTeacher;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedpassword");
        testUser.setEmail("test@example.com");
        testUser.setRole("STUDENT");

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setUser(testUser);
        testStudent.setStudentId("S12345");
        testStudent.setPhone("1234567890");
        testStudent.setAddress("Test Address");

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setUser(testUser);
        testTeacher.setEmployeeId("T12345");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Mathematics");
        testCourse.setCode("MATH101");
    }

    @Test
    void testRegisterStudent_Success() {
        when(userRepository.existsByUsername("newstudent")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(studentRepository.existsByStudentId("S99999")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return user;
        });
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        User result = userService.registerStudent("newstudent", "password", "new@example.com",
                "S99999", "555-1234", "New Address");

        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testRegisterStudent_UsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThatThrownBy(() ->
                userService.registerStudent("testuser", "password", "new@example.com",
                        "S99999", "555-1234", "New Address"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");
    }

    @Test
    void testGetUserByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        User result = userService.getUserByUsername("testuser");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByUsername("unknown"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void testGetStudentByUserId_Success() {
        when(studentRepository.findByUserId(1L)).thenReturn(Optional.of(testStudent));

        Student result = userService.getStudentByUserId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getStudentId()).isEqualTo("S12345");
    }

    @Test
    void testGetAllStudentDTOs() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(testStudent));

        List<StudentDTO> result = userService.getAllStudentDTOs();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
    }

    @Test
    void testUpdateStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        userService.updateStudent(1L, "updated@example.com", "999-9999", "Updated Address");

        assertThat(testStudent.getUser().getEmail()).isEqualTo("updated@example.com");
        assertThat(testStudent.getPhone()).isEqualTo("999-9999");
        assertThat(testStudent.getAddress()).isEqualTo("Updated Address");
    }

    @Test
    void testDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        doNothing().when(studentRepository).delete(any(Student.class));
        doNothing().when(userRepository).delete(any(User.class));

        userService.deleteStudent(1L);

        verify(studentRepository).delete(testStudent);
        verify(userRepository).delete(testUser);
    }

    @Test
    void testEnrollStudentInCourse() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        userService.enrollStudentInCourse(1L, 1L);

        assertThat(testStudent.getCourses()).contains(testCourse);
        verify(studentRepository).save(testStudent);
    }

    @Test
    void testRemoveStudentFromCourse() {
        testStudent.getCourses().add(testCourse);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        userService.removeStudentFromCourse(1L, 1L);

        assertThat(testStudent.getCourses()).doesNotContain(testCourse);
        verify(studentRepository).save(testStudent);
    }
}