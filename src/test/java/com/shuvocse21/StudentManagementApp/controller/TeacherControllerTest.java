package com.shuvocse21.StudentManagementApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.shuvocse21.StudentManagementApp.service.UserService;
import com.shuvocse21.StudentManagementApp.repository.CourseRepository;
import com.shuvocse21.StudentManagementApp.repository.StudentRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(roles = "TEACHER")
    void dashboard() throws Exception {
        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/dashboard"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void viewStudents() throws Exception {
        mockMvc.perform(get("/teacher/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/students"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addStudent() throws Exception {
        mockMvc.perform(post("/teacher/add-student")
                        .with(csrf())
                        .param("username", "newstudent")
                        .param("password", "pass123")
                        .param("email", "student@test.com")
                        .param("studentId", "S1001")
                        .param("phone", "555-1234")
                        .param("address", "123 College Ave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/students"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteStudent() throws Exception {
        mockMvc.perform(get("/teacher/delete-student/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/students"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void enrollStudent() throws Exception {
        mockMvc.perform(post("/teacher/enroll-student")
                        .with(csrf())
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/enroll-student"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void student_AccessingTeacherDashboard_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteStudent_WithInvalidId_ShouldHandleGracefully() throws Exception {
        mockMvc.perform(get("/teacher/delete-student/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/students"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void enrollStudent_WithInvalidIds_ShouldShowError() throws Exception {
        mockMvc.perform(post("/teacher/enroll-student")
                        .with(csrf())
                        .param("studentId", "9999")
                        .param("courseId", "9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/enroll-student"));

    }

    // NON-ESSENTIAL METHODS (Blank)
    @Test
    void showAddStudentForm() { }

    @Test
    void editStudentForm() { }
    @Test

    void updateStudent() { }

    @Test
    void viewCourses()  { }

    @Test
    void showAddCourseForm()  { }

    @Test
    void addCourse()  { }

    @Test
    void showEnrollStudentForm() { }
}