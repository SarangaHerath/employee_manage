package com.java.employment_system.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;
import java.util.List;

@Data
@Builder
public class CompensationBreakdownDto {
    private Long employeeId;
    private YearMonth month;
    private double totalCompensation;
    private List<CompensationDto> compensations;
}
