package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.requests.marketplace.UpdateLicenseRequest;
import com.WeAre.BeatGenius.api.responses.marketplace.LicenseResponse;
import com.WeAre.BeatGenius.domain.entities.License;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BeatMapper.class})
public interface LicenseMapper {
    @Mapping(target = "id", ignore = true)
    License toEntity(CreateLicenseRequest request);

    void updateEntityFromDto(UpdateLicenseRequest request, @MappingTarget License license);

    LicenseResponse toDto(License license);
}