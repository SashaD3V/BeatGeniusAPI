package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BeatMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producer", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Beat toEntity(CreateBeatRequest request);

    @Mapping(target = "producer", source = "producer")
    BeatResponse toDto(Beat beat);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "audioUrl", ignore = true)
    void updateEntityFromDto(UpdateBeatRequest request, @MappingTarget Beat beat);

    // Méthode utilitaire pour associer un producer à un beat
    default Beat setProducer(Beat beat, User producer) {
        beat.setProducer(producer);
        return beat;
    }
}