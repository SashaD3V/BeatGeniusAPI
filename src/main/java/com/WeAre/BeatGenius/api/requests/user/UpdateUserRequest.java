package com.WeAre.BeatGenius.api.requests.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String email;
    private String artistName;
    private String profilePicture;
    // Pas de password ici, ça devrait être un endpoint séparé pour la sécurité
}