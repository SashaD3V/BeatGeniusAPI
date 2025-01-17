package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.dto.requests.user.CreateUserRequest;
import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(source = "artistName", target = "artistName")
  @Mapping(target = "id", ignore = true)
  @Mapping(
      target = "username",
      ignore = true) // On ignore username puisqu'il n'existe pas dans la request
  @Mapping(target = "beats", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  User toEntity(CreateUserRequest request);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "artistName", target = "artistName")
  @Mapping(source = "profilePicture", target = "profilePicture")
  @Mapping(source = "role", target = "role")
  UserResponse toResponse(User user);
}
