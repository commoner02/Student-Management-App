package com.shuvocse21.StudentManagementApp.repository;

import com.shuvocse21.StudentManagementApp.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherId(Long teacherId);
}