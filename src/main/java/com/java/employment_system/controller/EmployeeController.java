package com.java.employment_system.controller;

import com.java.employment_system.dto.request.RequestEmployeeDto;
import com.java.employment_system.dto.response.CommonResponse;
import com.java.employment_system.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/employee")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping("/addEmployee")
    public ResponseEntity<CommonResponse> addEmployee(@RequestBody RequestEmployeeDto requestEmployeeDto) {
        log.info("hit add employee method dto:{}", requestEmployeeDto);
        return employeeService.addEmployee(requestEmployeeDto);
    }
    @GetMapping("/search")
    public ResponseEntity<CommonResponse> searchEmployees(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String position) {
       CommonResponse commonResponse = employeeService.searchEmployees(firstName, lastName, position);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/getEmployeeDetails")
    public ResponseEntity<CommonResponse> getEmployeeDetails(@RequestParam("id") Long id) {
        CommonResponse commonResponse = employeeService.getEmployeeDetails(id);
        return ResponseEntity.ok(commonResponse);
    }
    @PutMapping("/editEmployee")
    public ResponseEntity<CommonResponse> editEmployee(@RequestBody RequestEmployeeDto requestEmployeeDto) {
        CommonResponse commonResponse = employeeService.editEmployee(requestEmployeeDto);
        return ResponseEntity.status(commonResponse.responseCode).body(commonResponse);
    }
}
