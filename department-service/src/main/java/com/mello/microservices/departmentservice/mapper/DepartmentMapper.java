package com.mello.microservices.departmentservice.mapper;

import com.mello.microservices.departmentservice.dto.DepartmentDto;
import com.mello.microservices.departmentservice.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper
{
    DepartmentMapper MAPPER = Mappers.getMapper(DepartmentMapper.class);
    Department mapToDepartment(DepartmentDto departmentDto);
    DepartmentDto mapToDepartmentDto(Department department);
}
