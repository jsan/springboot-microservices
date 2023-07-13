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

    @PutMapping({"{id}"})
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @RequestBody DepartmentDto departmentDto)
    {
        return ResponseEntity.ok(departmentService.update(id, departmentDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<DepartmentDto> getDptById(@PathVariable Long id)
    {
        return new ResponseEntity<>(departmentService.getDptById(id), HttpStatus.OK);
    }

    @GetMapping("dpt/{departmentCode}")
    public ResponseEntity<DepartmentDto> getDptByDepartmentCode(@PathVariable String departmentCode)
    {
        return new ResponseEntity<>(departmentService.getDptByDepartmentCode(departmentCode), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<DepartmentDto>> listAll()
    {
        return ResponseEntity.ok(departmentService.listAll());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id)
    {
        departmentService.delete(id);
        return new ResponseEntity<>("Delete call ok.", HttpStatus.OK);
    }
}
