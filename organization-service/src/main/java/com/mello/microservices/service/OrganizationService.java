package com.mello.microservices.service;

import com.mello.microservices.dto.OrganizationDto;
import java.util.List;

public interface OrganizationService {
    OrganizationDto saveOrganization(OrganizationDto organizationDto);
    OrganizationDto getOrganizationByCode(String organizationCode);
    List<OrganizationDto> getAllOrganizations();
}
