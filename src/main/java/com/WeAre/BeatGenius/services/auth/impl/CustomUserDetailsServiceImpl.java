package com.WeAre.BeatGenius.services.auth.impl;

import com.WeAre.BeatGenius.domain.repositories.UserRepository;
import com.WeAre.BeatGenius.services.auth.interfaces.ICustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements ICustomUserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Override
  public UserDetails loadUserById(String id) {
    try {
      Long userId = Long.parseLong(id);
      return userRepository
          .findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    } catch (NumberFormatException e) {
      throw new UsernameNotFoundException("Invalid user ID format");
    }
  }
}
