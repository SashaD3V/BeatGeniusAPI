package com.WeAre.BeatGenius.api.dto.responses.auth;

import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import lombok.Data;

@Data
public class AuthResponse {
  private String token;
  private UserResponse user;
}
