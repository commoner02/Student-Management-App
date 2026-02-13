package com.shuvocse21.StudentManagementApp.service;

import com.shuvocse21.StudentManagementApp.dto.CourseDTO;
import com.shuvocse21.StudentManagementApp.dto.StudentDTO;
import com.shuvocse21.StudentManagementApp.entity.*;
import com.shuvocse21.StudentManagementApp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public User registerStudent(String username, String password, String email,
                                String studentId, String phone, String address) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        if (studentRepository.existsByStudentId(studentId)) {
            throw new RuntimeException("Student ID already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("STUDENT");
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        student.setStudentId(studentId);
        student.setPhone(phone);
        student.setAddress(address);
        studentRepository.save(student);

        return savedUser;
    }

    @Transactional
    public User registerTeacher(String username, String password, String email, String employeeId) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        if (teacherRepository.existsByEmployeeId(employeeId)) {
            throw new RuntimeException("Employee ID already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("TEACHER");
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setEmployeeId(employeeId);
        teacherRepository.save(teacher);

        return savedUser;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // FIXED: Added this missing method
    public Student getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public StudentDTO getStudentDTOByUserId(Long userId) {
        Student student = getStudentByUserId(userId);
        return convertToStudentDTO(student);
    }

    public List<StudentDTO> getAllStudentDTOs() {
        return studentRepository.findAll().stream()
                .map(this::convertToStudentDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getAllCourseDTOs() {
        return courseRepository.findAll().stream()
                .map(this::convertToCourseDTO)
                .collect(Collectors.toList());
    }

    private StudentDTO convertToStudentDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setUsername(student.getUser().getUsername());
        dto.setEmail(student.getUser().getEmail());
        dto.setStudentId(student.getStudentId());
        dto.setPhone(student.getPhone());
        dto.setAddress(student.getAddress());
        dto.setCourseCodes(student.getCourses().stream()
                .map(Course::getCode)
                .collect(Collectors.toList()));
        return dto;
    }

    private CourseDTO convertToCourseDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setCode(course.getCode());
        dto.setTeacherName(course.getTeacher() != null ?
                course.getTeacher().getUser().getUsername() : "Not Assigned");
        dto.setStudentCount(course.getStudents().size());
        return dto;
    }

    @Transactional
    public void updateStudent(Long studentId, String email, String phone, String address) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.getUser().setEmail(email);
        student.setPhone(phone);
        student.setAddress(address);
        userRepository.save(student.getUser());
        studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        User user = student.getUser();
        studentRepository.delete(student);
        userRepository.delete(user);
    }

    @Transactional
    public void enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            studentRepository.save(student);
        }
    }

    @Transactional
    public void removeStudentFromCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        student.getCourses().remove(course);
        studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}