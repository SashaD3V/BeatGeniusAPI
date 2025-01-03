package com.WeAre.BeatGenius.infrastructure.security;

import com.WeAre.BeatGenius.services.auth.interfaces.ICustomUserDetailsService;
import com.WeAre.BeatGenius.services.auth.interfaces.IJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final ICustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Header d'autorisation manquant ou invalide.");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Auth Header: " + authHeader);

        try {
            final String jwt = authHeader.substring(7);
            System.out.println("JWT: " + jwt);

            final String userId = jwtService.extractUserId(jwt);
            System.out.println("User ID (extrait du token): " + userId);

            if (userId == null) {
                System.out.println("ERREUR : userId est null !");
                throw new ServletException("Invalid token");
            }

            UserDetails userDetails = customUserDetailsService.loadUserById(userId);
            System.out.println("UserDetails (chargé par loadUserById): " + userDetails);

            if (userDetails == null) {
                System.out.println("ERREUR : userDetails est null !");
                throw new ServletException("User not found");
            }

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication Token: " + authToken);

                filterChain.doFilter(request, response);
            } else {
                throw new ServletException("Invalid token");
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}