package com.java.employment_system.service.impl;

import com.java.employment_system.dto.request.CompensationBreakdownDto;
import com.java.employment_system.dto.request.CompensationDto;
import com.java.employment_system.dto.request.CompensationHistoryDto;
import com.java.employment_system.dto.request.RequestCompensationDto;
import com.java.employment_system.dto.response.CommonResponse;
import com.java.employment_system.entity.Compensation;
import com.java.employment_system.entity.Employee;
import com.java.employment_system.repository.CompensationRepo;
import com.java.employment_system.repository.EmployeeRepo;
import com.java.employment_system.service.CompensationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompensationServiceImpl implements CompensationService {

    private final CompensationRepo compensationRepo;
    private final EmployeeRepo employeeRepo;

    public CompensationServiceImpl(CompensationRepo compensationRepo, EmployeeRepo employeeRepo) {
        this.compensationRepo = compensationRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public ResponseEntity<CommonResponse> addCompensation(RequestCompensationDto requestCompensationDto) {
        try {
            Employee employee = employeeRepo.findById(requestCompensationDto.getEmpId())
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));


            Compensation compensation = Compensation.builder()
                    .type(requestCompensationDto.getType())
                    .amount(requestCompensationDto.getAmount())
                    .description(requestCompensationDto.getDescription())
                    .date(requestCompensationDto.getDate())
                    .employee(employee)
                    .build();


            compensationRepo.save(compensation);


            CommonResponse commonResponse = CommonResponse.builder()
                    .responseCode(HttpStatus.CREATED)
                    .message("Compensation details added successfully")
                    .data(compensation)
                    .build();

            return ResponseEntity.status(commonResponse.responseCode).body(commonResponse);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.builder()
                            .responseCode(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message("An error occurred while adding compensation details")
                            .build());
        }
    }

    @Override
    public ResponseEntity<CommonResponse> viewCompensationHistory(Long employeeId, YearMonth startMonth, YearMonth endMonth) {
        try {
            Employee employee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

            List<CompensationHistoryDto> compensationHistory = fetchCompensationHistory(employee, startMonth, endMonth);

            CommonResponse commonResponse = CommonResponse.builder()
                    .responseCode(HttpStatus.OK)
                    .message("Compensation history retrieved successfully")
                    .data(compensationHistory)
                    .build();

            return ResponseEntity.ok(commonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.builder()
                            .responseCode(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message("An error occurred while retrieving compensation history")
                            .build());
        }
    }

    @Override
    public ResponseEntity<CommonResponse> viewCompensationBreakdown(Long employeeId, YearMonth month) {
        try {
            Employee employee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

            List<Compensation> compensationsForMonth = fetchCompensationsForMonth(employee, month);

            CompensationBreakdownDto breakdownDto = CompensationBreakdownDto.builder()
                    .employeeId(employeeId)
                    .month(month)
                    .totalCompensation(calculateTotalCompensation(compensationsForMonth))
                    .compensations(compensationsForMonth.stream()
                            .map(this::mapToCompensationDto)
                            .collect(Collectors.toList()))
                    .build();

            CommonResponse commonResponse = CommonResponse.builder()
                    .responseCode(HttpStatus.OK)
                    .message("Compensation breakdown retrieved successfully")
                    .data(breakdownDto)
                    .build();

            return ResponseEntity.ok(commonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.builder()
                            .responseCode(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message("An error occurred while retrieving compensation breakdown")
                            .build());
        }
    }

    private List<Compensation> fetchCompensationsForMonth(Employee employee, YearMonth month) {
        return employee.getCompensations().stream()
                .filter(compensation -> compensation.getDate().equals(month))
                .collect(Collectors.toList());
    }

    private double calculateTotalCompensation(List<Compensation> compensations) {
        return compensations.stream()
                .mapToDouble(Compensation::getAmount)
                .sum();
    }

    private CompensationDto mapToCompensationDto(Compensation compensation) {
        return CompensationDto.builder()
                .type(compensation.getType())
                .amount(compensation.getAmount())
                .description(compensation.getDescription())
                .build();
    }

    private List<CompensationHistoryDto> fetchCompensationHistory(Employee employee, YearMonth startMonth, YearMonth endMonth) {
        List<CompensationHistoryDto> historyList = new ArrayList<>();

        for (YearMonth currentMonth = startMonth; !currentMonth.isAfter(endMonth); currentMonth = currentMonth.plusMonths(1)) {
            double totalCompensation = calculateTotalCompensationForMonth(employee, currentMonth);
            CompensationHistoryDto historyDto = CompensationHistoryDto.builder()
                    .year(currentMonth.getYear())
                    .month(currentMonth.getMonthValue())
                    .totalCompensation(totalCompensation)
                    .build();
            historyList.add(historyDto);
        }

        return historyList;
    }

    private double calculateTotalCompensationForMonth(Employee employee, YearMonth month) {
        List<Compensation> compensationsForMonth = employee.getCompensations().stream()
                .filter(compensation -> isSameMonth(compensation.getDate(), month))
                .collect(Collectors.toList());

        return compensationsForMonth.stream()
                .mapToDouble(Compensation::getAmount)
                .sum();
    }

    private boolean isSameMonth(String compensationDate, YearMonth targetMonth) {
        YearMonth yearMonthFromCompensation = YearMonth.parse(compensationDate);
        return yearMonthFromCompensation.equals(targetMonth);
    }


}
