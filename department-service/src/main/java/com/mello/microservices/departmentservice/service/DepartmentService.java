package com.mello.microservices.departmentservice.service;

import com.mello.microservices.departmentservice.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService
{
    DepartmentDto save(DepartmentDto departmentDto);
    DepartmentDto update(Long id, DepartmentDto departmentDto);
    DepartmentDto getDptById(Long id);
    DepartmentDto getDptByDepartmentCode(String id);
    List<DepartmentDto> listAll();
    void delete(Long id);
}
