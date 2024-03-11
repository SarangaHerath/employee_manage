package com.java.employment_system.service;

import com.java.employment_system.dto.request.RequestEmployeeDto;
import com.java.employment_system.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {
    ResponseEntity<CommonResponse> addEmployee(RequestEmployeeDto requestEmployeeDto);

    CommonResponse searchEmployees(String firstName, String lastName, String position);

    CommonResponse getEmployeeDetails(Long id);

    CommonResponse editEmployee(RequestEmployeeDto requestEmployeeDto);
}
