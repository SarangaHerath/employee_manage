package com.java.employment_system.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompensationHistoryDto {
    private int year;
    private int month;
    private double totalCompensation;
}
