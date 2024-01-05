package com.mello.microservices.controller;

import com.adyen.service.exception.ApiException;
import com.mello.microservices.dto.OrganizationDto;
import com.mello.microservices.dto.OrganizationsDto;
import lombok.AllArgsConstructor;
import com.mello.microservices.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("organizations")
@AllArgsConstructor
@CrossOrigin
public class OrganizationController {

    private OrganizationService organizationService;

    // Build Save Organization REST API
    @PostMapping
    public ResponseEntity<OrganizationDto> saveOrganization(@RequestBody OrganizationDto organizationDto){
        OrganizationDto savedOrganization = organizationService.saveOrganization(organizationDto);
        return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    }

    // Build Get Organization by Code REST API
    @GetMapping("{code}")
    public ResponseEntity<OrganizationDto> getOrganization(@PathVariable("code") String organizationCode){
        System.out.println("Docker version...!");
        OrganizationDto organizationDto = organizationService.getOrganizationByCode(organizationCode);
        return ResponseEntity.ok(organizationDto);
    }

    @GetMapping
    public ResponseEntity<OrganizationsDto> getAllOrganizations(){
        return new ResponseEntity<>(new OrganizationsDto(organizationService.getAllOrganizations()), HttpStatus.OK);
    }

    // @GetMapping("/payment")
    // public ResponseEntity<String> sendPayment() throws IOException, ApiException
    // {
    //     System.out.println("foi");
    //     organizationService.sendPayment();
    //     return ResponseEntity.ok("Sent");
    // }

}
