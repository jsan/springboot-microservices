package com.mello.microservices.employeeservice.service.impl;

import com.mello.microservices.employeeservice.dto.APIResponseDto;
import com.mello.microservices.employeeservice.dto.DepartmentDto;
import com.mello.microservices.employeeservice.dto.EmployeeDto;
import com.mello.microservices.employeeservice.dto.OrganizationDto;
import com.mello.microservices.employeeservice.entity.Employee;
import com.mello.microservices.employeeservice.mapper.EmployeeMapper;
import com.mello.microservices.employeeservice.repository.EmployeeRepository;
import com.mello.microservices.employeeservice.service.client.DepartmentAPIClient;
import com.mello.microservices.employeeservice.service.EmployeeService;
import com.mello.microservices.employeeservice.service.client.OrganizationAPIClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private EmployeeRepository employeeRepository;

/*  Not used:
    private RestTemplate restTemplate;

    private WebClient webClient;
*/

    private DepartmentAPIClient departmentAPIClient;
    private OrganizationAPIClient organizationAPIClient;

    @Override
    public EmployeeDto save(EmployeeDto employeeDto)
    {
        Employee employee = employeeRepository.save(EmployeeMapper.MAPPER.mapToEmployee(employeeDto));
        return EmployeeMapper.MAPPER.mapToEmployeeDto(employee);
    }

    @Override
    public EmployeeDto update(Long id, EmployeeDto employeeDto)
    {
        // TODO Validate ID with FindById throw exception
        employeeDto.setId(id);
        Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        return EmployeeMapper.MAPPER.mapToEmployeeDto(employeeRepository.save(employee));
    }

    @Override
    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    // @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    public APIResponseDto  getEmployeeById(Long id)
    {
        // TODO add exception handling (orElseThrow) case employee not found (and also on All other methods of this class)

        LOGGER.warn("Inside getEmployeeById method");
        EmployeeDto employeeDto = EmployeeMapper.MAPPER.mapToEmployeeDto(employeeRepository.findById(id).get());

        // RestTemplate call:
        // ResponseEntity<DepartmentDto> departmentDtoCall =
        //         restTemplate.getForEntity("http://localhost:8080/departments/dpt/" + employeeDto.getDepartmentCode(), DepartmentDto.class);
        //
        // DepartmentDto departmentDto = departmentDtoCall.getBody();

        // Webclient call:
        // DepartmentDto departmentDto = webClient.get()
        //         .uri("http://localhost:8083/organizations/" + organizationCode)
        //         .retrieve()
        //         .bodyToMono(OrganizationDto.class).block();

        // Http FeignClient API call:
        DepartmentDto departmentDto = departmentAPIClient.getDptByDepartmentCode(employeeDto.getDepartmentCode());

        OrganizationDto organizationDto = getOrganization(employeeDto.getOrganizationCode());

        return new APIResponseDto(employeeDto, departmentDto, organizationDto);
    }

    public APIResponseDto  getDefaultDepartment(Long id, Exception e){

        LOGGER.warn("Inside getDefaultDepartment method");

        // Employee
        EmployeeDto employeeDto = EmployeeMapper.MAPPER.mapToEmployeeDto(employeeRepository.findById(id).get());

        // Department
        DepartmentDto defaultDepartmentDto = new DepartmentDto(1L,
                "Default department name",
                "Default department desc",
                "Default department code");

        // Organization
        OrganizationDto organizationDto = getOrganization(employeeDto.getOrganizationCode());

        return new APIResponseDto(employeeDto, defaultDepartmentDto, organizationDto);
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

    private OrganizationDto getOrganization(String organizationCode)
    {
        return  organizationAPIClient.getOrganization(organizationCode);
    }
}
