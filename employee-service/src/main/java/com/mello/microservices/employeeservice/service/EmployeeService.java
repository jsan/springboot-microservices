package com.mello.microservices.employeeservice.service;

import com.mello.microservices.employeeservice.dto.APIResponseDto;
import com.mello.microservices.employeeservice.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService
{
    EmployeeDto save(EmployeeDto employeeDto);
    EmployeeDto update (Long id, EmployeeDto employeeDto);
    APIResponseDto getEmployeeById(Long id);
    List<EmployeeDto> listAll();
    void delete(Long id);
}
