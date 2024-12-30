package com.WeAre.BeatGenius.api.responses.user;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String artistName;
    private String profilePicture;
    // On n'inclut pas le password dans la response pour des raisons de sécurité
}