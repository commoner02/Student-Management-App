package com.shuvocse21.StudentManagementApp.controller;

import com.shuvocse21.StudentManagementApp.entity.*;
import com.shuvocse21.StudentManagementApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;
    private final com.shuvocse21.StudentManagementApp.repository.DepartmentRepository departmentRepository;
    private final com.shuvocse21.StudentManagementApp.repository.CourseRepository courseRepository;
    private final com.shuvocse21.StudentManagementApp.repository.TeacherRepository teacherRepository;
    private final com.shuvocse21.StudentManagementApp.repository.StudentRepository studentRepository;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "teacher/dashboard";
    }

    @GetMapping("/students")
    public String viewStudents(Model model) {
        List<Student> students = userService.getAllStudents();
        model.addAttribute("students", students);
        return "teacher/students";
    }

    @GetMapping("/add-student")
    public String showAddStudentForm(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "teacher/add-student";
    }

    @PostMapping("/add-student")
    public String addStudent(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String studentId,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam Long departmentId,
            RedirectAttributes redirectAttributes) {

        try {
            userService.registerStudent(username, password, email, studentId, phone, address, departmentId);
            redirectAttributes.addFlashAttribute("success", "Student added successfully!");
            return "redirect:/teacher/students";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/teacher/add-student";
        }
    }

    @GetMapping("/edit-student/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = userService.getStudentById(id);
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("student", student);
        model.addAttribute("departments", departments);
        return "teacher/edit-student";
    }

    @PostMapping("/update-student/{id}")
    public String updateStudent(
            @PathVariable Long id,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam Long departmentId,
            RedirectAttributes redirectAttributes) {

        userService.updateStudent(id, email, phone, address, departmentId);
        redirectAttributes.addFlashAttribute("success", "Student updated successfully!");
        return "redirect:/teacher/students";
    }

    @GetMapping("/delete-student/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        return "redirect:/teacher/students";
    }

    // Course Management
    @GetMapping("/courses")
    public String viewCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "teacher/courses";
    }

    @GetMapping("/add-course")
    public String showAddCourseForm(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "teacher/add-course";
    }

    @PostMapping("/add-course")
    public String addCourse(
            @RequestParam String name,
            @RequestParam String code,
            @RequestParam Long departmentId,
            RedirectAttributes redirectAttributes) {

        try {
            Course course = new Course();
            course.setName(name);
            course.setCode(code);

            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            course.setDepartment(department);

            courseRepository.save(course);
            redirectAttributes.addFlashAttribute("success", "Course added successfully!");
            return "redirect:/teacher/courses";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/teacher/add-course";
        }
    }

    // Department Management
    @GetMapping("/departments")
    public String viewDepartments(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "teacher/departments";
    }

    @GetMapping("/add-department")
    public String showAddDepartmentForm() {
        return "teacher/add-department";
    }

    @PostMapping("/add-department")
    public String addDepartment(
            @RequestParam String name,
            @RequestParam String code,
            RedirectAttributes redirectAttributes) {

        try {
            Department department = new Department();
            department.setName(name);
            department.setCode(code);

            departmentRepository.save(department);
            redirectAttributes.addFlashAttribute("success", "Department added successfully!");
            return "redirect:/teacher/departments";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/teacher/add-department";
        }
    }

    // Course Enrollment Management
    @GetMapping("/enroll-student")
    public String showEnrollStudentForm(Model model) {
        List<Student> students = userService.getAllStudents();
        List<Course> courses = courseRepository.findAll();

        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        return "teacher/enroll-student";
    }

    @PostMapping("/enroll-student")
    public String enrollStudentToCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            RedirectAttributes redirectAttributes) {

        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Check if already enrolled
            if (student.getCourses().contains(course)) {
                redirectAttributes.addFlashAttribute("error", "Student is already enrolled in this course!");
                return "redirect:/teacher/enroll-student";
            }

            // Enroll student in course
            student.getCourses().add(course);
            studentRepository.save(student);

            redirectAttributes.addFlashAttribute("success",
                    "Student " + student.getStudentId() + " successfully enrolled in " + course.getCode() + "!");
            return "redirect:/teacher/enroll-student";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/teacher/enroll-student";
        }
    }

    @GetMapping("/view-student-courses/{studentId}")
    public String viewStudentCourses(@PathVariable Long studentId, Model model) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Course> allCourses = courseRepository.findAll();

        // Filter out courses the student is already enrolled in
        List<Course> availableCourses = allCourses.stream()
                .filter(course -> !student.getCourses().contains(course))
                .collect(Collectors.toList());

        model.addAttribute("student", student);
        model.addAttribute("availableCourses", availableCourses);
        return "teacher/manage-student-courses";
    }

    @PostMapping("/enroll-student-course/{studentId}")
    public String enrollStudentInCourse(
            @PathVariable Long studentId,
            @RequestParam Long courseId,
            RedirectAttributes redirectAttributes) {

        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Check if already enrolled
            if (student.getCourses().contains(course)) {
                redirectAttributes.addFlashAttribute("error", "Student is already enrolled in this course!");
                return "redirect:/teacher/view-student-courses/" + studentId;
            }

            // Enroll student in course
            student.getCourses().add(course);
            studentRepository.save(student);

            redirectAttributes.addFlashAttribute("success",
                    "Successfully enrolled in " + course.getCode() + "!");
            return "redirect:/teacher/view-student-courses/" + studentId;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/teacher/view-student-courses/" + studentId;
        }
    }

    @GetMapping("/remove-course-from-student/{studentId}/{courseId}")
    public String removeCourseFromStudent(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            RedirectAttributes redirectAttributes) {

        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Remove course from student's courses
            student.getCourses().remove(course);
            studentRepository.save(student);

            redirectAttributes.addFlashAttribute("success",
                    "Successfully removed from " + course.getCode() + "!");
            return "redirect:/teacher/view-student-courses/" + studentId;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/teacher/view-student-courses/" + studentId;
        }
    }

    @GetMapping("/view-course-students/{courseId}")
    public String viewCourseStudents(@PathVariable Long courseId, Model model) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        model.addAttribute("course", course);
        return "teacher/view-course-students";
    }

    // Fixed: This is the second removeStudentFromCourse method - kept original name
    @GetMapping("/remove-student-from-course/{courseId}/{studentId}")
    public String removeStudentFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            RedirectAttributes redirectAttributes) {

        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // Remove student from course
            student.getCourses().remove(course);
            studentRepository.save(student);

            redirectAttributes.addFlashAttribute("success",
                    "Student " + student.getStudentId() + " removed from course!");
            return "redirect:/teacher/view-course-students/" + courseId;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/teacher/view-course-students/" + courseId;
        }
    }
}