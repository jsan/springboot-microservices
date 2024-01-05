package com.mello.microservices.departmentservice.controller;

import com.mello.microservices.departmentservice.dto.DepartmentDto;
import com.mello.microservices.departmentservice.dto.DepartmentsDto;
import com.mello.microservices.departmentservice.service.DepartmentService;
import com.mello.microservices.employeeservice.dto.EmployeeDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.net.URI;

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

    @CrossOrigin
    @GetMapping("all")
    public ResponseEntity<DepartmentsDto> listAll()
    {
        return ResponseEntity.ok(new DepartmentsDto(departmentService.listAll()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id)
    {
        departmentService.delete(id);
        return new ResponseEntity<>("Delete call ok.", HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/payment")
    public ModelAndView sendPayment(@RequestBody EmployeeDto employeeDto, HttpServletRequest request) throws IOException, ApiException
    {
        System.out.println("foi");
        // departmentService.sendOrder(employeeDto);
        request.setAttribute(
                View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/redirectedPostToPost");
        // response.setHeader("Location", "http://localhost:3000/employees");
        // response.setStatus(302);
        // // return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/employees")).build();
        // return ResponseEntity.ok("Sent");
    }
    @PostMapping("/redirectedPostToPost")
    public ModelAndView redirectedPostToPost() {
        return new ModelAndView("http://localhost:3000/employees");
    }

}
