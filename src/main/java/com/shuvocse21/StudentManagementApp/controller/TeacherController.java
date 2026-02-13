package com.shuvocse21.StudentManagementApp.controller;

import com.shuvocse21.StudentManagementApp.dto.CourseDTO;
import com.shuvocse21.StudentManagementApp.dto.StudentDTO;
import com.shuvocse21.StudentManagementApp.entity.*;
import com.shuvocse21.StudentManagementApp.service.UserService;
import com.shuvocse21.StudentManagementApp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<CourseDTO> courses = userService.getAllCourseDTOs();
        List<StudentDTO> students = userService.getAllStudentDTOs();
        model.addAttribute("courses", courses);
        model.addAttribute("students", students);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("totalCourses", courses.size());
        return "teacher/dashboard";
    }

    @GetMapping("/students")
    public String viewStudents(Model model) {
        model.addAttribute("students", userService.getAllStudentDTOs());
        return "teacher/students";
    }

    @GetMapping("/add-student")
    public String showAddStudentForm() {
        return "teacher/add-student";
    }

    @PostMapping("/add-student")
    public String addStudent(@RequestParam String username, @RequestParam String password,
                             @RequestParam String email, @RequestParam String studentId,
                             @RequestParam String phone, @RequestParam String address,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.registerStudent(username, password, email, studentId, phone, address);
            redirectAttributes.addFlashAttribute("success", "Student added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teacher/students";
    }

    @GetMapping("/edit-student/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = userService.getStudentById(id);
        model.addAttribute("student", student);
        return "teacher/edit-student";
    }

    @PostMapping("/update-student/{id}")
    public String updateStudent(@PathVariable Long id, @RequestParam String email,
                                @RequestParam String phone, @RequestParam String address,
                                RedirectAttributes redirectAttributes) {
        userService.updateStudent(id, email, phone, address);
        redirectAttributes.addFlashAttribute("success", "Student updated successfully!");
        return "redirect:/teacher/students";
    }

    @GetMapping("/delete-student/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        return "redirect:/teacher/students";
    }

    @GetMapping("/courses")
    public String viewCourses(Model model) {
        model.addAttribute("courses", userService.getAllCourseDTOs());
        return "teacher/courses";
    }

    @GetMapping("/add-course")
    public String showAddCourseForm() {
        return "teacher/add-course";
    }

    @PostMapping("/add-course")
    public String addCourse(@RequestParam String name, @RequestParam String code,
                            RedirectAttributes redirectAttributes) {
        try {
            Course course = new Course();
            course.setName(name);
            course.setCode(code);
            courseRepository.save(course);
            redirectAttributes.addFlashAttribute("success", "Course added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teacher/courses";
    }

    @GetMapping("/enroll-student")
    public String showEnrollStudentForm(Model model) {
        model.addAttribute("students", userService.getAllStudentDTOs());
        model.addAttribute("courses", userService.getAllCourseDTOs());
        return "teacher/enroll-student";
    }

    @PostMapping("/enroll-student")
    public String enrollStudent(@RequestParam Long studentId, @RequestParam Long courseId,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.enrollStudentInCourse(studentId, courseId);
            redirectAttributes.addFlashAttribute("success", "Student enrolled successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teacher/enroll-student";
    }

    @GetMapping("/remove-enrollment/{studentId}/{courseId}")
    public String removeEnrollment(@PathVariable Long studentId, @PathVariable Long courseId,
                                   RedirectAttributes redirectAttributes) {
        try {
            userService.removeStudentFromCourse(studentId, courseId);
            redirectAttributes.addFlashAttribute("success", "Enrollment removed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teacher/enroll-student";
    }
}