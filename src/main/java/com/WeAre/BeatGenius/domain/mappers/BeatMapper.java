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
    uses = {UserMapper.class, LicenseMapper.class})
public interface BeatMapper
    extends BaseMapper<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest> {

  @Override
  @Mapping(source = "createdAt", target = "createdAt") // ajout
  @Mapping(source = "updatedAt", target = "updatedAt") // ajout
  @Mapping(source = "version", target = "version") // ajout
  @Mapping(source = "producer", target = "producer")
  @Mapping(source = "licenses", target = "licenses")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "audioUrl", target = "audioUrl")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "tags", target = "tags")
  @Mapping(source = "bpm", target = "bpm")
  @Mapping(source = "note", target = "note")
  @Mapping(source = "scale", target = "scale")
  @Mapping(source = "moods", target = "moods")
  @Mapping(source = "instruments", target = "instruments")
  @Mapping(source = "releaseDate", target = "releaseDate")
  @Mapping(source = "includeForBulkDiscounts", target = "includeForBulkDiscounts")
  @Mapping(source = "genre", target = "genre")
  BeatResponse toDto(Beat beat);

  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "producer", ignore = true)
  @Mapping(target = "collaborations", ignore = true)
  @Mapping(target = "licenses", ignore = true)
  Beat toEntity(CreateBeatRequest request);

  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "producer", ignore = true)
  @Mapping(target = "collaborations", ignore = true)
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
