package com.mello.microservices.departmentservice.service;

import com.mello.microservices.departmentservice.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService
{
    DepartmentDto save(DepartmentDto departmentDto);
    DepartmentDto getDptoById(Long id);
    List<DepartmentDto> listAll();
    void delete(Long id);
}
