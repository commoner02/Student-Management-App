package com.shuvocse21.StudentManagementApp.repository;

import com.shuvocse21.StudentManagementApp.entity.Course;
import com.shuvocse21.StudentManagementApp.entity.Teacher;
import com.shuvocse21.StudentManagementApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;

    // ESSENTIAL METHOD
    @Test
    void findByTeacherId() {
        User user = new User();
        user.setUsername("professorsmith");
        user.setPassword("pass123");
        user.setEmail("smith@school.com");
        user.setRole("TEACHER");
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setEmployeeId("T2001");
        Teacher savedTeacher = teacherRepository.save(teacher);

        Course course1 = new Course();
        course1.setName("Mathematics");
        course1.setCode("MATH201");
        course1.setTeacher(savedTeacher);
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setName("Physics");
        course2.setCode("PHY201");
        course2.setTeacher(savedTeacher);
        courseRepository.save(course2);

        List<Course> foundCourses = courseRepository.findByTeacherId(savedTeacher.getId());

        assertThat(foundCourses).hasSize(2);
        assertThat(foundCourses).extracting(Course::getCode)
                .containsExactlyInAnyOrder("MATH201", "PHY201");
    }

}