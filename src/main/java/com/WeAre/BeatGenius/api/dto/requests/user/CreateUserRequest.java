package com.WeAre.BeatGenius.api.dto.requests.user;

import com.WeAre.BeatGenius.domain.enums.UserRole;
import lombok.Data;

@Data
public class CreateUserRequest {
  private String email;
  private String password;
  private String artistName;
  private String profilePicture;
  private UserRole role;
}
