package com.mello.microservices.service.impl;

import com.mello.microservices.dto.OrganizationDto;
import com.mello.microservices.entity.Organization;
import com.mello.microservices.mapper.OrganizationMapper;
import lombok.AllArgsConstructor;
import com.mello.microservices.repository.OrganizationRepository;
import com.mello.microservices.service.OrganizationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationRepository organizationRepository;

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
}
