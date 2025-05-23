package com.pruebatecnica.pruebatecnica.service.impl;


import com.pruebatecnica.pruebatecnica.service.IAuthService;
import com.pruebatecnica.pruebatecnica.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @Override
  public String authenticateUser(String email, String password) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password)
    );

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return jwtService.generateToken(userDetails);
  }
}