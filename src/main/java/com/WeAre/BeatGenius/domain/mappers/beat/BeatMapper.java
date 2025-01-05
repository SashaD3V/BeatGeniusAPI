package com.WeAre.BeatGenius.domain.mappers.beat;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.enums.Genre;
import com.WeAre.BeatGenius.domain.enums.Note;
import com.WeAre.BeatGenius.domain.enums.Scale;
import com.WeAre.BeatGenius.domain.mappers.BaseMapper;
import com.WeAre.BeatGenius.domain.mappers.LicenseMapper;
import com.WeAre.BeatGenius.domain.mappers.UserMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

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

  @Named("createRequestFromParams")
  default CreateBeatRequest toCreateRequest(
      String title,
      Genre genre,
      String description,
      String audioUrl, // déjà validé et stocké
      Integer bpm,
      Note note,
      Scale scale,
      List<String> tags,
      List<String> moods,
      List<String> instruments,
      LocalDateTime releaseDate,
      Boolean includeForBulkDiscounts) {
    return CreateBeatRequest.builder()
        .title(title)
        .genre(genre)
        .description(description)
        .audioUrl(audioUrl)
        .bpm(bpm)
        .note(note)
        .scale(scale)
        .tags(tags)
        .moods(moods)
        .instruments(instruments)
        .releaseDate(releaseDate)
        .includeForBulkDiscounts(includeForBulkDiscounts)
        .build();
  }
}
