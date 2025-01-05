// SecurityConstants.java
package com.WeAre.BeatGenius.domain.constants;

/** Constantes liées à la sécurité et l'authentification */
public final class SecurityConstants {
  private SecurityConstants() {}

  // JWT
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";

  // Rôles
  public static final String ROLE_PREFIX = "ROLE_";
  public static final String PRODUCER_ROLE = ROLE_PREFIX + "PRODUCER";
  public static final String USER_ROLE = ROLE_PREFIX + "USER";
  public static final String ADMIN_ROLE = ROLE_PREFIX + "ADMIN";

  // Routes publiques
  public static final String[] PUBLIC_ROUTES = {
    "/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**"
  };
}
