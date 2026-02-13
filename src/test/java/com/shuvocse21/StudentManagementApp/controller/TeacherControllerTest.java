package com.shuvocse21.StudentManagementApp.controller;

import com.shuvocse21.StudentManagementApp.dto.CourseDTO;
import com.shuvocse21.StudentManagementApp.dto.StudentDTO;
import com.shuvocse21.StudentManagementApp.entity.Course;
import com.shuvocse21.StudentManagementApp.service.UserService;
import com.shuvocse21.StudentManagementApp.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.shuvocse21.StudentManagementApp.config.TestSecurityConfig;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
@Import(TestSecurityConfig.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CourseRepository courseRepository;

    private StudentDTO testStudentDTO;
    private CourseDTO testCourseDTO;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testStudentDTO = new StudentDTO();
        testStudentDTO.setId(1L);
        testStudentDTO.setUsername("student1");
        testStudentDTO.setEmail("student@example.com");
        testStudentDTO.setStudentId("S12345");
        testStudentDTO.setPhone("1234567890");
        testStudentDTO.setAddress("Test Address");
        testStudentDTO.setCourseCodes(Arrays.asList("MATH101"));

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Mathematics");
        testCourse.setCode("MATH101");

        testCourseDTO = new CourseDTO();
        testCourseDTO.setId(1L);
        testCourseDTO.setName("Mathematics");
        testCourseDTO.setCode("MATH101");
        testCourseDTO.setTeacherName("Not Assigned");
        testCourseDTO.setStudentCount(0);
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDashboard() throws Exception {
        when(userService.getAllCourseDTOs()).thenReturn(Arrays.asList(testCourseDTO));
        when(userService.getAllStudentDTOs()).thenReturn(Arrays.asList(testStudentDTO));

        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/dashboard"))
                .andExpect(model().attributeExists("courses", "students", "totalStudents", "totalCourses"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testViewStudents() throws Exception {
        when(userService.getAllStudentDTOs()).thenReturn(Arrays.asList(testStudentDTO));

        mockMvc.perform(get("/teacher/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/students"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testAddStudentForm() throws Exception {
        mockMvc.perform(get("/teacher/add-student"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/add-student"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testAddStudent_Success() throws Exception {
        mockMvc.perform(post("/teacher/add-student")
                        .with(csrf())
                        .param("username", "newstudent")
                        .param("password", "password")
                        .param("email", "new@example.com")
                        .param("studentId", "S99999")
                        .param("phone", "555-1234")
                        .param("address", "New Address"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/students"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testViewCourses() throws Exception {
        when(userService.getAllCourseDTOs()).thenReturn(Arrays.asList(testCourseDTO));

        mockMvc.perform(get("/teacher/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/courses"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testAddCourse_Success() throws Exception {
        mockMvc.perform(post("/teacher/add-course")
                        .with(csrf())
                        .param("name", "Physics")
                        .param("code", "PHY101"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/courses"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testEnrollStudentForm() throws Exception {
        when(userService.getAllStudentDTOs()).thenReturn(Arrays.asList(testStudentDTO));
        when(userService.getAllCourseDTOs()).thenReturn(Arrays.asList(testCourseDTO));

        mockMvc.perform(get("/teacher/enroll-student"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/enroll-student"))
                .andExpect(model().attributeExists("students", "courses"));
    }
}