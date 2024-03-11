package com.java.employment_system.controller;

import com.java.employment_system.dto.request.RequestCompensationDto;
import com.java.employment_system.dto.request.RequestEmployeeDto;
import com.java.employment_system.dto.response.CommonResponse;
import com.java.employment_system.service.CompensationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

@RestController
@CrossOrigin
@RequestMapping("api/v1/compensation")
@Slf4j
public class CompensationController {
    private final CompensationService compensationService;

    public CompensationController(CompensationService compensationService) {
        this.compensationService = compensationService;
    }
    @PostMapping("/addCompensation")
    public ResponseEntity<CommonResponse> addCompensation(@RequestBody RequestCompensationDto requestCompensationDto) {
        log.info("hit add employee method dto:{}", requestCompensationDto);
        return compensationService.addCompensation(requestCompensationDto);
    }
    @GetMapping("/viewCompensationHistory")
    public ResponseEntity<CommonResponse> viewCompensationHistory(
            @RequestParam("empId") Long empId,
            @RequestParam("startMonth") String startMonth,
            @RequestParam("endMonth") String endMonth) {

        log.info("Hit view compensation history method. Employee ID: {}, Start Month: {}, End Month: {}", empId, startMonth, endMonth);

        try {
            YearMonth startYearMonth = YearMonth.parse(startMonth);
            YearMonth endYearMonth = YearMonth.parse(endMonth);

            return compensationService.viewCompensationHistory(empId, startYearMonth, endYearMonth);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format. Use YYYY-MM for startMonth and endMonth.");
            return ResponseEntity.badRequest().body(
                    CommonResponse.builder()
                            .responseCode(HttpStatus.BAD_REQUEST)
                            .message("Invalid date format. Use YYYY-MM for startMonth and endMonth.")
                            .build()
            );
        }
    }
    @GetMapping("/breakdown")
    public ResponseEntity<CommonResponse> viewCompensationBreakdown(
            @RequestParam Long employeeId,
            @RequestParam String selectedMonth) {
        log.info("Hit view compensation breakdown method for employeeId: {} and month: {}", employeeId, selectedMonth);

        // Convert the selectedMonth String to YearMonth
        YearMonth month = YearMonth.parse(selectedMonth);

        return compensationService.viewCompensationBreakdown(employeeId, month);
    }
}
