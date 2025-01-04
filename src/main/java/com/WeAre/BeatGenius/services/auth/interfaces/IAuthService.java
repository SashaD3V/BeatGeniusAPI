package com.WeAre.BeatGenius.services.auth.interfaces;

import com.WeAre.BeatGenius.api.dto.requests.auth.LoginRequest;
import com.WeAre.BeatGenius.api.dto.requests.auth.RegisterRequest;
import com.WeAre.BeatGenius.api.dto.responses.auth.AuthResponse;

public interface IAuthService {
  AuthResponse register(RegisterRequest request);

  AuthResponse login(LoginRequest request);
}
