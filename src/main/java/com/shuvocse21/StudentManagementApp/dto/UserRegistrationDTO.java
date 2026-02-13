package com.shuvocse21.StudentManagementApp.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String role;
    private String studentId;
    private String employeeId;
    private String phone;
    private String address;
}