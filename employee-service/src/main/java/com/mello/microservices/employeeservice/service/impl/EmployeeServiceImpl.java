package com.mello.microservices.employeeservice.service.impl;

import com.mello.microservices.employeeservice.dto.APIResponseDto;
import com.mello.microservices.employeeservice.dto.DepartmentDto;
import com.mello.microservices.employeeservice.mapper.EmployeeMapper;
import com.mello.microservices.employeeservice.repository.EmployeeRepository;
import com.mello.microservices.employeeservice.dto.EmployeeDto;
import com.mello.microservices.employeeservice.entity.Employee;
import com.mello.microservices.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService
{
    private EmployeeRepository employeeRepository;

    private RestTemplate restTemplate;

    @Override
    public EmployeeDto save(EmployeeDto employeeDto)
    {
        Employee employee = employeeRepository.save(EmployeeMapper.MAPPER.mapToEmployee(employeeDto));
        return EmployeeMapper.MAPPER.mapToEmployeeDto(employee);
    }

    @Override
    public APIResponseDto  getEmployeeById(Long id)
    {
        // TODO add exception (orElseThrow) case employee not found (and also on All other methods of this class)

        EmployeeDto employeeDto = EmployeeMapper.MAPPER.mapToEmployeeDto(employeeRepository.findById(id).get());

        ResponseEntity<DepartmentDto> dptoDto =
                restTemplate.getForEntity("http://localhost:8080/departments/" + employeeDto.getDepartmentCode(), DepartmentDto.class);

        DepartmentDto departmentDto = dptoDto.getBody();

        return new APIResponseDto(employeeDto, departmentDto);

    }

    @Override
    public List<EmployeeDto> listAll(){
        List<Employee> list = employeeRepository.findAll();
        return list.stream()
                .map(EmployeeMapper.MAPPER::mapToEmployeeDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id)
    {
        employeeRepository.deleteById(id);
    }
}
