package com.mello.microservices.employeeservice.mapper;

import com.mello.microservices.employeeservice.dto.EmployeeDto;
import com.mello.microservices.employeeservice.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper
{
    EmployeeMapper MAPPER = Mappers.getMapper(EmployeeMapper.class);
    Employee mapToEmployee(EmployeeDto EmployeeDto);
    EmployeeDto mapToEmployeeDto(Employee Employee);
}
