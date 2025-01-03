package com.WeAre.BeatGenius.api.dto.responses.user;

import com.WeAre.BeatGenius.domain.enums.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String artistName;
    private String profilePicture;
    private UserRole role;  // Ajout du rôle dans la response
}