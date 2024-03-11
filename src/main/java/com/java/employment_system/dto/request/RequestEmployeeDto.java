package com.java.employment_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RequestEmployeeDto {

    private Long  id;

    private String firstName;

    private String middleName;

    private String lastName;

    private Date birthday;

    private String position;
}
