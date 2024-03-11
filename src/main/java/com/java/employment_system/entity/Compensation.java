package com.java.employment_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Compensation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;

    private Double amount;

    private String description;

    private YearMonth date;

}
