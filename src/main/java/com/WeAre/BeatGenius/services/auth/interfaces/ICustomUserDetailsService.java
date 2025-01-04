package com.WeAre.BeatGenius.services.auth.interfaces;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ICustomUserDetailsService {
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

  UserDetails loadUserById(String id);
}
