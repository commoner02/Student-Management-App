package com.shuvocse21.StudentManagementApp.controller;

import com.shuvocse21.StudentManagementApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String role = auth.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_STUDENT")) {
                return "redirect:/student/dashboard";
            } else if (role.equals("ROLE_TEACHER")) {
                return "redirect:/teacher/dashboard";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/register/teacher")
    public String registerTeacher() {
        return "register-teacher";
    }

    @PostMapping("/register/teacher")
    public String registerTeacher(@RequestParam String username, @RequestParam String password,
                                  @RequestParam String email, @RequestParam String employeeId,
                                  RedirectAttributes redirectAttributes) {
        try {
            userService.registerTeacher(username, password, email, employeeId);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register/teacher";
        }
        return "redirect:/login";
    }
}