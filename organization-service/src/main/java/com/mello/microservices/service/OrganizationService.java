package com.mello.microservices.service;

import com.adyen.service.exception.ApiException;
import com.mello.basedomains.basedomains.dto.OrderEvent;
import com.mello.microservices.dto.OrganizationDto;

import java.io.IOException;
import java.util.List;

public interface OrganizationService {
    OrganizationDto saveOrganization(OrganizationDto organizationDto);
    OrganizationDto getOrganizationByCode(String organizationCode);
    List<OrganizationDto> getAllOrganizations();
    void sendPayment(OrderEvent orderEvent) throws IOException, ApiException;
}
