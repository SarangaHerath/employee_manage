package com.java.employment_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "emp_id")
    private Long id;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "middle_name",nullable = true)
    private String middleName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "birthday",nullable = false)
    private Date birthday;

    @Column(name = "position",nullable = false)
    private String position;
}
