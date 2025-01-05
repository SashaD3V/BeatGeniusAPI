package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, LicenseMapper.class, BeatCreditMapper.class})
public interface BeatMapper
        extends BaseMapper<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest> {

  @Override
  @Mapping(source = "producer", target = "producer")
  @Mapping(source = "licenses", target = "licenses")
  @Mapping(source = "beatCredits", target = "beatCredits")
  @Mapping(source = "note", target = "note")
  @Mapping(source = "scale", target = "scale")
  BeatResponse toDto(Beat beat);

  @Override
  @Mapping(target = "producer", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "beatCredits", ignore = true)
  @Mapping(target = "licenses", ignore = true)
  @Mapping(source = "note", target = "note")
  @Mapping(source = "scale", target = "scale")
  Beat toEntity(CreateBeatRequest request);

  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "producer", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "beatCredits", ignore = true)
  @Mapping(target = "licenses", ignore = true)
  @Mapping(target = "audioUrl", ignore = true)
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "bpm", ignore = true)
  @Mapping(target = "note", ignore = true)
  @Mapping(target = "scale", ignore = true)
  @Mapping(target = "moods", ignore = true)
  @Mapping(target = "instruments", ignore = true)
  @Mapping(target = "releaseDate", ignore = true)
  @Mapping(target = "includeForBulkDiscounts", ignore = true)
  void updateEntityFromDto(UpdateBeatRequest request, @MappingTarget Beat beat);
}