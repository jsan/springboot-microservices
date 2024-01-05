package com.mello.microservices.service.impl;

import com.adyen.service.exception.ApiException;
import com.mello.basedomains.basedomains.dto.OrderEvent;
import com.mello.microservices.dto.OrganizationDto;
import com.mello.microservices.entity.Organization;
import com.mello.microservices.mapper.OrganizationMapper;
import com.mello.microservices.payment.AdyenPaymentProvider;
import lombok.AllArgsConstructor;
import com.mello.microservices.repository.OrganizationRepository;
import com.mello.microservices.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);
    private OrganizationRepository organizationRepository;
    private AdyenPaymentProvider adyenPaymentProvider;

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {

        // convert OrganizationDto into Organization jpa entity
        Organization organization = OrganizationMapper.mapToOrganization(organizationDto);

        Organization savedOrganization = organizationRepository.save(organization);

        return OrganizationMapper.mapToOrganizationDto(savedOrganization);
    }

    @Override
    public OrganizationDto getOrganizationByCode(String organizationCode) {
        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);
        return OrganizationMapper.mapToOrganizationDto(organization);
    }

    @Override
    public List<OrganizationDto> getAllOrganizations()
    {
        List<Organization> organizationList = organizationRepository.findAll();
        return organizationList.stream()
                .map(OrganizationMapper::mapToOrganizationDto)
                .collect(Collectors.toList());
    }

    @Override
    public void sendPayment(OrderEvent orderEvent) throws IOException, ApiException
    {
        // TODO: Create redirect to success or failed FE page depending on status from payment
        String status = adyenPaymentProvider.sendPaymentRequest();

        int random = (int)(Math.random()*1000);

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setOrganizationCode(String.valueOf(random));
        organizationDto.setOrganizationName(orderEvent.getOrder().getName());
        organizationDto.setOrganizationDescription(status);

        // convert OrganizationDto into Organization jpa entity
        Organization organization = OrganizationMapper.mapToOrganization(organizationDto);

        Organization savedOrganization = organizationRepository.save(organization);



        LOGGER.info(String.format("JSON persist message: %s - %s", orderEvent.getMessage(), orderEvent.getOrder().getName()));

    }
}
