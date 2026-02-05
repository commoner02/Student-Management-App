package com.shuvocse21.StudentManagementApp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String studentId;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String departmentName;
    private Long departmentId;
}