package com.shuvocse21.StudentManagementApp.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String studentId;
    private String employeeId;
    private String phone;
    private Long departmentId;

}