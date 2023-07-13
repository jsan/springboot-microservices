package com.mello.microservices.employeeservice.service.impl;

import com.mello.microservices.employeeservice.dto.APIResponseDto;
import com.mello.microservices.employeeservice.dto.DepartmentDto;
import com.mello.microservices.employeeservice.dto.EmployeeDto;
import com.mello.microservices.employeeservice.entity.Employee;
import com.mello.microservices.employeeservice.mapper.EmployeeMapper;
import com.mello.microservices.employeeservice.repository.EmployeeRepository;
import com.mello.microservices.employeeservice.service.APIClient;
import com.mello.microservices.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService
{
    private EmployeeRepository employeeRepository;

/*  Not used:
    private RestTemplate restTemplate;
    private WebClient webClient;
*/

    private APIClient apiClient;

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
    public APIResponseDto  getEmployeeById(Long id)
    {
        // TODO add exception (orElseThrow) case employee not found (and also on All other methods of this class)

        EmployeeDto employeeDto = EmployeeMapper.MAPPER.mapToEmployeeDto(employeeRepository.findById(id).get());

/*      RestTemplate:
        ResponseEntity<DepartmentDto> departmentDtoCall =
                restTemplate.getForEntity("http://localhost:8080/departments/dpt/" + employeeDto.getDepartmentCode(), DepartmentDto.class);

        DepartmentDto departmentDto = departmentDtoCall.getBody();

        WebClient(WebFlux):
        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/departments/dpt/" + employeeDto.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class).block();
*/

        DepartmentDto departmentDto = apiClient.getDptByDepartmentCode(employeeDto.getDepartmentCode());

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
