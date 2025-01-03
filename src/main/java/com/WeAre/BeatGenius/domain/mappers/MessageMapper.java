package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.dto.requests.messaging.CreateMessageRequest;
import com.WeAre.BeatGenius.api.dto.responses.messaging.MessageResponse;
import com.WeAre.BeatGenius.domain.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", ignore = true)  // On l'assignera dans le service
    @Mapping(target = "recipient", ignore = true)  // On l'assignera dans le service
    @Mapping(target = "sentAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "read", constant = "false")  // Message non lu par défaut
    Message toEntity(CreateMessageRequest request);

    MessageResponse toResponse(Message message);
}