package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.requests.messaging.CreateMessageRequest;
import com.WeAre.BeatGenius.api.requests.messaging.UpdateMessageRequest;
import com.WeAre.BeatGenius.api.responses.messaging.MessageResponse;
import com.WeAre.BeatGenius.domain.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    @Mapping(target = "read", constant = "false")
    Message toEntity(CreateMessageRequest request);

    void updateEntityFromDto(UpdateMessageRequest request, @MappingTarget Message message);

    MessageResponse toDto(Message message);
}