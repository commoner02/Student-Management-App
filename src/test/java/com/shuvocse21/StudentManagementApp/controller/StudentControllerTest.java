package com.shuvocse21.StudentManagementApp.controller;

import com.shuvocse21.StudentManagementApp.dto.StudentDTO;
import com.shuvocse21.StudentManagementApp.entity.Student;
import com.shuvocse21.StudentManagementApp.entity.User;
import com.shuvocse21.StudentManagementApp.service.UserService;
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

@WebMvcTest(StudentController.class)
@Import(TestSecurityConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser;
    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("student1");
        testUser.setPassword("password");
        testUser.setEmail("student@example.com");
        testUser.setRole("STUDENT");

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setUser(testUser);
        testStudent.setStudentId("S12345");
        testStudent.setPhone("1234567890");
        testStudent.setAddress("Test Address");

        testStudentDTO = new StudentDTO();
        testStudentDTO.setId(1L);
        testStudentDTO.setUsername("student1");
        testStudentDTO.setEmail("student@example.com");
        testStudentDTO.setStudentId("S12345");
        testStudentDTO.setPhone("1234567890");
        testStudentDTO.setAddress("Test Address");
        testStudentDTO.setCourseCodes(Arrays.asList("MATH101", "PHY101"));
    }

    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void testDashboard() throws Exception {
        when(userService.getUserByUsername("student1")).thenReturn(testUser);
        when(userService.getStudentDTOByUserId(1L)).thenReturn(testStudentDTO);

        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void testUpdateProfile() throws Exception {
        when(userService.getUserByUsername("student1")).thenReturn(testUser);
        when(userService.getStudentByUserId(1L)).thenReturn(testStudent);

        mockMvc.perform(post("/student/profile/update")
                        .with(csrf())
                        .param("email", "updated@example.com")
                        .param("phone", "9999999999")
                        .param("address", "Updated Address"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/dashboard"))
                .andExpect(flash().attributeExists("success"));
    }
}