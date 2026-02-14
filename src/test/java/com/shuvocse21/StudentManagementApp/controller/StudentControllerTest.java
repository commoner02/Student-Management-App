package com.shuvocse21.StudentManagementApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.shuvocse21.StudentManagementApp.service.UserService;
import com.shuvocse21.StudentManagementApp.dto.StudentDTO;
import com.shuvocse21.StudentManagementApp.entity.Student;
import com.shuvocse21.StudentManagementApp.entity.User;
import java.util.ArrayList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void dashboard() throws Exception {
        StudentDTO mockStudent = new StudentDTO();
        mockStudent.setUsername("student1");
        mockStudent.setStudentId("S1001");
        mockStudent.setEmail("student@test.com");
        mockStudent.setPhone("555-1234");
        mockStudent.setAddress("123 College Ave");
        mockStudent.setCourseCodes(new ArrayList<>());

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("student1");

        when(userService.getUserByUsername("student1")).thenReturn(mockUser);
        when(userService.getStudentDTOByUserId(1L)).thenReturn(mockStudent);

        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void updateProfile() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("student1");

        Student mockStudent = new Student();
        mockStudent.setId(1L);
        mockStudent.setUser(mockUser);

        when(userService.getUserByUsername("student1")).thenReturn(mockUser);
        when(userService.getStudentByUserId(1L)).thenReturn(mockStudent);

        mockMvc.perform(post("/student/profile/update")
                        .with(csrf())
                        .param("email", "updated@test.com")
                        .param("phone", "555-9999")
                        .param("address", "456 New St"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/dashboard"))
                .andExpect(flash().attributeExists("success"));
    }
}