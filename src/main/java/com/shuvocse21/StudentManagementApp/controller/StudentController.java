package com.shuvocse21.StudentManagementApp.controller;

import com.shuvocse21.StudentManagementApp.dto.StudentDTO;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return userService.getUserByUsername(auth.getName());
        }
        return null;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";

        StudentDTO studentDTO = userService.getStudentDTOByUserId(user.getId());
        model.addAttribute("student", studentDTO);
        return "student/dashboard";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String email, @RequestParam String phone,
                                @RequestParam String address, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";

        // FIXED: Using getStudentByUserId method which now exists
        Student student = userService.getStudentByUserId(user.getId());
        userService.updateStudent(student.getId(), email, phone, address);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/student/dashboard";
    }
}