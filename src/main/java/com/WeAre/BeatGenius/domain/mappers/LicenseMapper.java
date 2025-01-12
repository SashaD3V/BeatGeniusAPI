package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.UpdateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.responses.marketplace.LicenseResponse;
import com.WeAre.BeatGenius.domain.entities.License;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/*
@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
*/

/*
La configuration nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL définit comment MapStruct doit gérer les valeurs null pendant le mapping.
Voici les différentes stratégies possibles :

RETURN_NULL : Si la source est null, la cible sera null
RETURN_DEFAULT : Si la source est null, la cible prendra une valeur par défaut (0 pour les nombres, false pour les booléens, etc.)
*/
@Mapper(componentModel = "spring")
public interface LicenseMapper
    extends BaseMapper<License, LicenseResponse, CreateLicenseRequest, UpdateLicenseRequest> {

  @Override
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(source = "name", target = "name")
  @Mapping(source = "type", target = "type")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "fileFormat", target = "fileFormat")
  @Mapping(source = "rights", target = "rights")
  @Mapping(source = "isTagged", target = "isTagged")
  @Mapping(source = "contractTerms", target = "contractTerms")
  @Mapping(source = "distributionLimit", target = "distributionLimit")
  @Mapping(source = "beat.id", target = "beatId")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "id", target = "id")
  LicenseResponse toDto(License entity);

  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "beat", ignore = true)
  License toEntity(CreateLicenseRequest createDto);

  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "beat", ignore = true)
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "fileFormat", ignore = true)
  @Mapping(target = "isTagged", ignore = true)
  @Mapping(target = "contractTerms", ignore = true)
  @Mapping(target = "distributionLimit", ignore = true)
  void updateEntityFromDto(UpdateLicenseRequest updateDto, @MappingTarget License entity);
}
