package com.WeAre.BeatGenius.api.responses.auth;

import com.WeAre.BeatGenius.api.responses.user.UserResponse;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private UserResponse user;
}