package com.shuvocse21.StudentManagementApp.repository;

import com.shuvocse21.StudentManagementApp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setName("Mathematics");
        testCourse.setCode("MATH101");
        entityManager.persistAndFlush(testCourse);
    }

    @Test
    void testFindAll() {
        List<Course> courses = courseRepository.findAll();
        assertThat(courses).isNotEmpty();
        assertThat(courses.get(0).getCode()).isEqualTo("MATH101");
    }
}