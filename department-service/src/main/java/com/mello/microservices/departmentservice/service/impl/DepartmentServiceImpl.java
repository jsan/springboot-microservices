package com.mello.microservices.departmentservice.service.impl;

import com.mello.microservices.departmentservice.dto.DepartmentDto;
import com.mello.microservices.departmentservice.entity.Department;
import com.mello.microservices.departmentservice.mapper.DepartmentMapper;
import com.mello.microservices.departmentservice.repository.DepartmentRepository;
import com.mello.microservices.departmentservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// TODO add exception handling
@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService
{
    private DepartmentRepository departmentRepo;

    @Override
    public DepartmentDto save(DepartmentDto departmentDto)
    {
        Department department = DepartmentMapper.MAPPER.mapToDepartment(departmentDto);
        return DepartmentMapper.MAPPER.mapToDepartmentDto(departmentRepo.save(department));
    }

    @Override
    public DepartmentDto getDptoById(Long id)
    {
        return departmentRepo.findById(id).map(DepartmentMapper.MAPPER::mapToDepartmentDto).orElse(null);
    }

    @Override
    public List<DepartmentDto> listAll()
    {
        List<Department> list = departmentRepo.findAll();
        return list.stream().map(DepartmentMapper.MAPPER::mapToDepartmentDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id)
    {
        departmentRepo.deleteById(id);
    }

}
