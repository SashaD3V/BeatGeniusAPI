package com.WeAre.BeatGenius.api.dto.requests.auth;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}
