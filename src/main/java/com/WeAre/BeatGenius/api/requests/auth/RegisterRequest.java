package com.WeAre.BeatGenius.api.requests.auth;

import com.WeAre.BeatGenius.domain.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String artistName;  // Ajout du champ manquant
    private UserRole role;
}