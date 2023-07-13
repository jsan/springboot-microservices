package com.mello.microservices.employeeservice.service;

import com.mello.microservices.employeeservice.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url="http://localhost:8080", value = "placeholder")
public interface APIClient
{
    @GetMapping("departments/dpt/{departmentCode}")
    DepartmentDto getDptByDepartmentCode(@PathVariable String departmentCode);
}
