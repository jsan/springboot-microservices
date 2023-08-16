package com.mello.microservices.employeeservice.service.client;

import com.mello.microservices.employeeservice.dto.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient(name = "ORGANIZATION-SERVICE")
@FeignClient(url="http://localhost:8083/", value = "ORGANIZATION-SERVICE")
public interface OrganizationAPIClient
{
    @GetMapping("organizations/{code}")
    OrganizationDto getOrganization(@PathVariable("code") String organizationCode);
}
