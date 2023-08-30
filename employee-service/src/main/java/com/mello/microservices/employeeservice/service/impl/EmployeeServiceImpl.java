package com.mello.microservices.employeeservice.service.impl;

import com.mello.microservices.employeeservice.dto.*;
import com.mello.microservices.employeeservice.entity.Employee;
import com.mello.microservices.employeeservice.kafka.KafkaEmployeeProducer;
import com.mello.microservices.employeeservice.mapper.EmployeeMapper;
import com.mello.microservices.employeeservice.repository.EmployeeRepository;
import com.mello.microservices.employeeservice.service.client.DepartmentAPIClient;
import com.mello.microservices.employeeservice.service.EmployeeService;
import com.mello.microservices.employeeservice.service.client.OrganizationAPIClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private EmployeeRepository employeeRepository;
    private DepartmentAPIClient departmentAPIClient;
    private OrganizationAPIClient organizationAPIClient;
    private KafkaEmployeeProducer kafkaEmployeeProducer;

/*  Not used:
    private RestTemplate restTemplate;
    private WebClient webClient;
*/
    @Override
    public EmployeeDto save(EmployeeDto employeeDto)
    {
        Employee employee = employeeRepository.save(EmployeeMapper.MAPPER.mapToEmployee(employeeDto));
        return EmployeeMapper.MAPPER.mapToEmployeeDto(employee);
    }

    @Override
    public Boolean update(Long id, EmployeeDto employeeDto)
    {
        // TODO Validate ID with FindById throw exception

        Optional<Employee> emp = employeeRepository.findById(id);

        if (emp.isPresent())
        {
            employeeDto.setId(emp.get().getId());

            sendKafkaMessage(employeeDto);

            Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
            EmployeeMapper.MAPPER.mapToEmployeeDto(employeeRepository.save(employee));

            return true;
        }
        return false;
    }

    private void sendKafkaMessage(EmployeeDto employeeDto)
    {
        EmployeeEvent employeeEvent = new EmployeeEvent();
        employeeEvent.setStatus("PENDENT");
        employeeEvent.setMessage("Employee Pending");
        employeeEvent.setEmployeeDto(employeeDto);

        kafkaEmployeeProducer.sendMessage(employeeEvent);
    }

    @Override
    // @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    public APIResponseDto  getEmployeeById(Long id)
    {
        // TODO add exception handling (orElseThrow) case employee not found (and also on All other methods of this class)

        LOGGER.warn("Inside getEmployeeById method");
        Employee emp = employeeRepository.findById(id).orElseThrow();
        EmployeeDto employeeDto = EmployeeMapper.MAPPER.mapToEmployeeDto(employeeRepository.findById(id).orElseThrow());

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

        // Http FeignClient API call:
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
