package com.WeAre.BeatGenius.services.auth.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String extractUserId(String token);
    boolean isTokenValid(String token, UserDetails userDetails); // Ajout de UserDetails comme paramètre
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
}