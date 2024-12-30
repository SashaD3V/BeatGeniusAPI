package com.WeAre.BeatGenius.services.auth;

import com.WeAre.BeatGenius.api.requests.auth.LoginRequest;
import com.WeAre.BeatGenius.api.requests.auth.RegisterRequest;
import com.WeAre.BeatGenius.api.responses.auth.AuthResponse;
import com.WeAre.BeatGenius.api.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());        // Ajout de l'email
        user.setArtistName(request.getArtistName()); // Ajout de l'artistName
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(mapToUserResponse(savedUser));

        return response;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(mapToUserResponse(user));

        return response;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());           // Au lieu de username
        response.setArtistName(user.getArtistName()); // Ajout des autres champs de votre UserResponse
        response.setProfilePicture(user.getProfilePicture());
        return response;
    }
}