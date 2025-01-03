package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.entities.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BeatMapper extends BaseMapper<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest> {
    @Override
    @Mapping(source = "producer", target = "producer")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdAt", target = "createdAt")
    BeatResponse toDto(Beat beat);

    @Override
    @Mapping(target = "producer", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Beat toEntity(CreateBeatRequest request);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "audioUrl", ignore = true)
    void updateEntityFromDto(UpdateBeatRequest request, @MappingTarget Beat beat);
}