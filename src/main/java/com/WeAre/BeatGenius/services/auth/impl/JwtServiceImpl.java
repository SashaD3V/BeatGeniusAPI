package com.WeAre.BeatGenius.services.auth.impl;

import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.services.auth.interfaces.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements IJwtService {
  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private Long jwtExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private Long refreshTokenExpiration; // Changé de 'long' à 'Long'

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  @Override
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    if (userDetails instanceof User) {
      User user = (User) userDetails;
      claims.put("role", user.getRole().name());
      claims.put("username", user.getUsername());
    }

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(((User) userDetails).getId().toString()) // Utilise l'ID comme subject
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String generateRefreshToken(UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(new HashMap<>())
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(
            new Date(System.currentTimeMillis() + refreshTokenExpiration)) // Correction ici
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String extractUserId(String token) {
    return extractAllClaims(token).getSubject();
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      final String userId = extractUserId(token);
      Claims claims = extractAllClaims(token);
      Date expirationDate = claims.getExpiration();

      // Compare l'ID au lieu du username
      return (userId.equals(((User) userDetails).getId().toString()))
          && !expirationDate.before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
