package com.java.employment_system.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompensationDto {
    private String type;
    private Double amount;
    private String description;
}
