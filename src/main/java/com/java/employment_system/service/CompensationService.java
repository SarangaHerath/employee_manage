package com.java.employment_system.service;

import com.java.employment_system.dto.request.RequestCompensationDto;
import com.java.employment_system.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;

import java.time.YearMonth;

public interface CompensationService {
    ResponseEntity<CommonResponse> addCompensation(RequestCompensationDto requestCompensationDto);

    ResponseEntity<CommonResponse> viewCompensationHistory(Long empId, YearMonth startYearMonth, YearMonth endYearMonth);

    ResponseEntity<CommonResponse> viewCompensationBreakdown(Long employeeId, YearMonth month);
}
