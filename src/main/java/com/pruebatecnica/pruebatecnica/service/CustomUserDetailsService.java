package com.pruebatecnica.pruebatecnica.service;


import com.pruebatecnica.pruebatecnica.respository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Para los roles, ya que en este proyecto no se definieron roles complejos

/**
 * Implementación personalizada de UserDetailsService para cargar los detalles del usuario
 * desde la base de datos para Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    return userRepository.findByEmail(email)
        .map(user -> {

          return new org.springframework.security.core.userdetails.User(
              user.getEmail(),
              user.getPassword(),
              user.isActive(),
              true, true, true,
              new ArrayList<>()
          );
        })
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + email));
  }
}