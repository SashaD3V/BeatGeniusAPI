package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.requests.user.CreateUserRequest;
import com.WeAre.BeatGenius.api.requests.user.UpdateUserRequest;
import com.WeAre.BeatGenius.api.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "artistName", source = "artistName")
    @Mapping(target = "profilePicture", source = "profilePicture")
    UserResponse toDto(User user);
}