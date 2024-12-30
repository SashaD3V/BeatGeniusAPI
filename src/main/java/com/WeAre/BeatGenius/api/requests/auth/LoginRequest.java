package com.WeAre.BeatGenius.api.requests.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}