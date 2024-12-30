package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BeatMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producer", ignore = true)
    Beat toEntity(CreateBeatRequest request);

    BeatResponse toDto(Beat beat);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(UpdateBeatRequest request, @MappingTarget Beat beat);
}