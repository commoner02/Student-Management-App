package com.shuvocse21.StudentManagementApp.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String name;
    private String code;
    private Long departmentId;
    private String departmentName;
    private Long teacherId;
    private String teacherName;
}