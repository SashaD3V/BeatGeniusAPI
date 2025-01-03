package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.UpdateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.responses.marketplace.LicenseResponse;
import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.Beat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LicenseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beat", ignore = true)
    License toEntity(CreateLicenseRequest request);

    @Mapping(target = "beatId", source = "beat.id")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "rights", source = "rights")
    LicenseResponse toResponse(License license);

    // Méthode utilitaire pour mettre à jour une licence existante
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beat", ignore = true)
    void updateFromRequest(UpdateLicenseRequest request, @MappingTarget License license);

    // Méthode utilitaire pour associer un Beat à une License
    default License associateBeat(License license, Beat beat) {
        license.setBeat(beat);
        return license;
    }
}