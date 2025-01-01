package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.requests.user.CreateUserRequest;
import com.WeAre.BeatGenius.api.requests.user.UpdateUserRequest;
import com.WeAre.BeatGenius.api.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beats", ignore = true)
    @Mapping(target = "username", source = "email")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "role", source = "role")  // Explicite le mapping du role
    User toEntity(CreateUserRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "artistName", source = "artistName")
    @Mapping(target = "profilePicture", source = "profilePicture")
    @Mapping(target = "role", source = "role", defaultExpression = "java(user.getRole())")  // Force le mapping avec une expression par défaut
    UserResponse toResponse(User user);

    @AfterMapping
    default void setRole(@MappingTarget UserResponse response, User user) {
        response.setRole(user.getRole());  // Assure le mapping manuel du role
    }
}