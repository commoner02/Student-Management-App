package com.shuvocse21.StudentManagementApp.controller;

import com.shuvocse21.StudentManagementApp.entity.Student;
import com.shuvocse21.StudentManagementApp.entity.User;
import com.shuvocse21.StudentManagementApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final UserService userService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            return userService.getUserByUsername(username);
        }
        return null;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        Student student = userService.getStudentByUserId(user.getId());
        model.addAttribute("student", student);
        return "student/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            RedirectAttributes redirectAttributes) {

        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        Student student = userService.getStudentByUserId(user.getId());

        // Update email in User entity
        student.getUser().setEmail(email);

        // Update phone and address in Student entity
        student.setPhone(phone);
        student.setAddress(address);

        // Save the changes through service
        userService.updateStudent(student.getId(), email, phone, address, student.getDepartment().getId());

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/student/profile";
    }
}