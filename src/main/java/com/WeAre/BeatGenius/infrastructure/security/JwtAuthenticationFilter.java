// JwtAuthenticationFilter.java
package com.WeAre.BeatGenius.infrastructure.security;

import com.WeAre.BeatGenius.services.auth.interfaces.ICustomUserDetailsService;
import com.WeAre.BeatGenius.services.auth.interfaces.IJwtService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final IJwtService jwtService;
  private final ICustomUserDetailsService customUserDetailsService;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      HttpServletResponse httpResponse = (HttpServletResponse) response;

      final String authHeader = httpRequest.getHeader("Authorization");

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        logger.debug("Header d'autorisation manquant ou invalide.");
        chain.doFilter(request, response);
        return;
      }

      final String jwt = authHeader.substring(7);
      final String userId = jwtService.extractUserId(jwt);

      if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);

        if (userDetails != null && jwtService.isTokenValid(jwt, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
          SecurityContextHolder.getContext().setAuthentication(authToken);
          logger.debug("Authentification réussie pour l'utilisateur: {}", userId);
        }
      }

      chain.doFilter(request, response);
    } catch (Exception e) {
      logger.error("Erreur d'authentification", e);
      SecurityContextHolder.clearContext();
      ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  @Override
  public void destroy() {}
}