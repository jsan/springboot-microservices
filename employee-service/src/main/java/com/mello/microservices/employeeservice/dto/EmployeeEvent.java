package com.mello.microservices.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEvent
{
    private String status;
    private String message;
    private EmployeeDto employeeDto;
}
