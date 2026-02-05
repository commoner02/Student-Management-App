package com.shuvocse21.StudentManagementApp.controller;

import com.shuvocse21.StudentManagementApp.entity.Department;
import com.shuvocse21.StudentManagementApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final com.shuvocse21.StudentManagementApp.repository.DepartmentRepository departmentRepository;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // TEACHER REGISTRATION ENDPOINTS
    @GetMapping("/register/teacher")
    public String registerTeacher(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "register-teacher";
    }

    @PostMapping("/register/teacher")
    public String registerTeacherUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String employeeId,
            @RequestParam Long departmentId,
            RedirectAttributes redirectAttributes) {

        try {
            userService.registerTeacher(username, password, email, employeeId, departmentId);
            redirectAttributes.addFlashAttribute("success", "Teacher registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register/teacher";
        }
    }
}