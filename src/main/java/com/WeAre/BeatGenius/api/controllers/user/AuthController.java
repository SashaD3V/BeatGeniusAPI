package com.WeAre.BeatGenius.api.controllers.user;

import com.WeAre.BeatGenius.api.dto.requests.auth.LoginRequest;
import com.WeAre.BeatGenius.api.dto.requests.auth.RegisterRequest;
import com.WeAre.BeatGenius.api.dto.responses.auth.AuthResponse;
import com.WeAre.BeatGenius.services.auth.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}