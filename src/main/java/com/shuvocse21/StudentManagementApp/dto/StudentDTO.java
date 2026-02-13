package com.shuvocse21.StudentManagementApp.dto;

import lombok.Data;
import java.util.List;

@Data
public class StudentDTO {
    private Long id;
    private String username;
    private String email;
    private String studentId;
    private String phone;
    private String address;
    private List<String> courseCodes;
}