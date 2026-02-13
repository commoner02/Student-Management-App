package com.shuvocse21.StudentManagementApp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "teachers")
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(unique = true, nullable = false)
    private String employeeId;
}