package com.mello.microservices.employeeservice.controller;

import com.mello.microservices.employeeservice.dto.APIResponseDto;
import com.mello.microservices.employeeservice.dto.EmployeeDto;
import com.mello.microservices.employeeservice.dto.Employees;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.mello.microservices.employeeservice.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("employees")
public class EmployeeController
{
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> save (@RequestBody EmployeeDto employeeDto)
    {
        return new ResponseEntity(employeeService.save(employeeDto), HttpStatus.CREATED);
    }

    @PutMapping({"{id}"})
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody EmployeeDto employeeDto)
    {
        return ResponseEntity.ok(employeeService.update(id, employeeDto));
    }

    @GetMapping
    public ResponseEntity<Employees> listAll()
    {
        Employees employees = new Employees(employeeService.listAll());
        return ResponseEntity.ok(employees);
    }

    @GetMapping("{id}")
    public ResponseEntity<APIResponseDto> getEmployeeById(@PathVariable Long id)
    {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        employeeService.delete(id);
        return ResponseEntity.ok("Delete ok");
    }

}

