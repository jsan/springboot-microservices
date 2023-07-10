package com.mello.microservices.departmentservice.controller;

import com.mello.microservices.departmentservice.dto.DepartmentDto;
import com.mello.microservices.departmentservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("departments")
@AllArgsConstructor
public class DepartmentController
{
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDto> save(@RequestBody DepartmentDto departmentDto)
    {
        return new ResponseEntity<>(departmentService.save(departmentDto), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DepartmentDto> getDptoById(@PathVariable Long id)
    {
        return new ResponseEntity<>(departmentService.getDptoById(id), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<DepartmentDto>> listAll () {
        return ResponseEntity.ok(departmentService.listAll());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        departmentService.delete(id);
        return new ResponseEntity<>("Delete call ok.",HttpStatus.OK);
    }
}
