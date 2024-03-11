package com.java.employment_system.service.impl;

import com.java.employment_system.dto.request.RequestEmployeeDto;
import com.java.employment_system.dto.response.CommonResponse;
import com.java.employment_system.entity.Employee;
import com.java.employment_system.repository.EmployeeRepo;
import com.java.employment_system.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public ResponseEntity<CommonResponse> addEmployee(RequestEmployeeDto requestEmployeeDto) {
        try {

            Optional<Employee> existingEmployee = employeeRepo.findByFirstNameAndLastNameAndMiddleNameAndBirthday(
                    requestEmployeeDto.getFirstName(),
                    requestEmployeeDto.getLastName(),
                    requestEmployeeDto.getMiddleName(),
                    requestEmployeeDto.getBirthday()
            );

            if (existingEmployee.isPresent()) {

                CommonResponse commonResponse = CommonResponse.builder()
                        .responseCode(HttpStatus.CONFLICT)
                        .message("Employee with the same name already exists")
                        .build();
                return ResponseEntity.status(commonResponse.responseCode).body(commonResponse);
            }

            log.info("Hit employee service impl dto: {}", requestEmployeeDto);
            Employee employee = Employee.builder()
                    .firstName(requestEmployeeDto.getFirstName())
                    .middleName(requestEmployeeDto.getMiddleName())
                    .lastName(requestEmployeeDto.getLastName())
                    .birthday(requestEmployeeDto.getBirthday())
                    .position(requestEmployeeDto.getPosition())
                    .build();
            employeeRepo.save(employee);

            CommonResponse commonResponse = CommonResponse.builder()
                    .data(employee)
                    .responseCode(HttpStatus.CREATED)
                    .message("Employee save success")
                    .build();

            return ResponseEntity.status(commonResponse.responseCode).body(commonResponse);


        } catch (Exception e) {
            throw new RuntimeException("Error occurred during add employee", e);
        }
    }

    @Override
    public CommonResponse searchEmployees(String firstName, String lastName, String position) {
        try {
            // Add logging before the query execution
            log.info("Searching for employees with firstName: {}, lastName: {}, position: {}", firstName, lastName, position);
            List<Employee> employees = employeeRepo.searchEmployees(firstName, lastName, position);



            String message;
            HttpStatus responseCode;

            if (employees.isEmpty()) {
                message = "0 results found";
                responseCode = HttpStatus.NOT_FOUND;
            } else {
                message = "Search results retrieved successfully";
                responseCode = HttpStatus.OK;
            }

            return CommonResponse.builder()
                    .data(employees)
                    .message(message)
                    .responseCode(responseCode)
                    .build();

        } catch (Exception e) {
            // Log the exception or handle it as appropriate for your application
            return CommonResponse.builder()
                    .message("An error occurred while processing the search request.")
                    .responseCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public CommonResponse getEmployeeDetails(Long id) {
        try {
            Optional<Employee> employeeOptional = employeeRepo.findById(id);

            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                return CommonResponse.builder()
                        .data(employee)
                        .responseCode(HttpStatus.OK)
                        .message("Employee details retrieved successfully")
                        .build();
            } else {
                return CommonResponse.builder()
                        .responseCode(HttpStatus.NOT_FOUND)
                        .message("Employee not found")
                        .build();
            }
        } catch (Exception e) {
            return CommonResponse.builder()
                    .responseCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("An error occurred while retrieving employee details")
                    .build();
        }
    }

    @Override
    public CommonResponse editEmployee(RequestEmployeeDto requestEmployeeDto) {
        try {
            Optional<Employee> existingEmployee = employeeRepo.findById(requestEmployeeDto.getId());

            if (existingEmployee.isEmpty()) {
                return CommonResponse.builder()
                        .responseCode(HttpStatus.NOT_FOUND)
                        .message("Employee not found for UID: " + requestEmployeeDto.getId())
                        .build();
            }

            Employee employeeToUpdate = existingEmployee.get();
            employeeToUpdate.setFirstName(requestEmployeeDto.getFirstName());
            employeeToUpdate.setMiddleName(requestEmployeeDto.getMiddleName());
            employeeToUpdate.setLastName(requestEmployeeDto.getLastName());
            employeeToUpdate.setBirthday(requestEmployeeDto.getBirthday());
            employeeToUpdate.setPosition(requestEmployeeDto.getPosition());

            Employee updatedEmployee = employeeRepo.save(employeeToUpdate);

            return CommonResponse.builder()
                    .responseCode(HttpStatus.OK)
                    .message("Employee details updated successfully")
                    .data(updatedEmployee)
                    .build();

        } catch (Exception e) {
            return CommonResponse.builder()
                    .responseCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("An error occurred while updating employee details")
                    .build();
        }
    }

}

