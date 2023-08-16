package com.mello.microservices.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrganizationsDto
{
    private List<OrganizationDto> organizations;

    public OrganizationsDto(List<OrganizationDto> organizations)
    {
        this.organizations = organizations;
    }

    public OrganizationsDto()
    {
    }
}
