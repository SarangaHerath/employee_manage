package com.java.employment_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RequestCompensationDto {

    private Long id;

    private String type;

    private Double amount;

    private String description;

    private String date;

    private Long empId;
}
