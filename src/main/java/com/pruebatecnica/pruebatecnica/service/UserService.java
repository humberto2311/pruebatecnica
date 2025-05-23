package com.pruebatecnica.pruebatecnica.service;


import com.pruebatecnica.pruebatecnica.dto.PasswordChangeDto;
import com.pruebatecnica.pruebatecnica.dto.UserRegistrationDto;
import com.pruebatecnica.pruebatecnica.exception.ResourceNotFoundException;
import com.pruebatecnica.pruebatecnica.model.User;
import com.pruebatecnica.pruebatecnica.respository.UserRepository;
import com.pruebatecnica.pruebatecnica.util.TokenBlacklist;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID; // Para generar tokens

@Service
public class UserService implements IUserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final IEmailService emailService;
  private final TokenBlacklist tokenBlacklist;



  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, IEmailService emailService, TokenBlacklist tokenBlacklist) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.tokenBlacklist = tokenBlacklist;
  }
/*
  @Override
  @Transactional
  public User registerUser(UserRegistrationDto registrationDto) {
    // Usando Optional y notación lambda
    userRepository.findByEmail(registrationDto.getEmail())
        .ifPresent(user -> {
          throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        });

    User user = new User();
    user.setFullName(registrationDto.getFullName());
    user.setEmail(registrationDto.getEmail());


    String registrationToken = UUID.randomUUID().toString();
    user.setRegistrationToken(registrationToken);
    user.setTokenCreationDate(LocalDateTime.now());
    user.setTokenExpirationDate(LocalDateTime.now().plusHours(24)); // Token válido por 24 horas
    user.setActive(false);

    User savedUser = userRepository.save(user);


    String activationLink = "http://localhost:8080/api/users/activate?token=" + registrationToken;
    emailService.sendActivationEmail(savedUser.getEmail(), savedUser.getFullName(), activationLink);

    return savedUser;
  }*/
@Override
@Transactional
public User registerUser(UserRegistrationDto registrationDto) {

  userRepository.findByEmail(registrationDto.getEmail())
      .ifPresent(user -> {
        throw new IllegalArgumentException("El correo electrónico ya está registrado.");
      });


  User user = new User();
  user.setFullName(registrationDto.getFullName());
  user.setEmail(registrationDto.getEmail());


  user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));


  String registrationToken = UUID.randomUUID().toString();
  user.setRegistrationToken(registrationToken);
  user.setTokenCreationDate(LocalDateTime.now());
  user.setTokenExpirationDate(LocalDateTime.now().plusHours(24));
  user.setActive(false);

  User savedUser = userRepository.save(user);

  // Enviar email de activación
  String activationLink = "http://localhost:8080/api/users/activate?token=" + registrationToken;
  emailService.sendActivationEmail(savedUser.getEmail(), savedUser.getFullName(), activationLink);

  return savedUser;
}

  @Override
  @Transactional
  public boolean activateUser(String token) {
    if (tokenBlacklist.isTokenBlacklisted(token)) {
      throw new IllegalArgumentException("Token ya utilizado o inválido.");
    }

    return userRepository.findByRegistrationToken(token)
        .filter(user -> !user.isActive() && user.getTokenExpirationDate().isAfter(LocalDateTime.now()))
        .map(user -> {
          user.setActive(true);
          user.setRegistrationToken(null); // Invalidar el token de registro
          user.setTokenCreationDate(null);
          user.setTokenExpirationDate(null);
          userRepository.save(user);
          tokenBlacklist.addTokenToBlacklist(token); // Añadir a la lista negra
          return true;
        })
        .orElseThrow(() -> new ResourceNotFoundException("Token de activación inválido o expirado."));
  }

  @Override
  @Transactional
  public boolean resetPassword(String email) {
    return userRepository.findByEmail(email)
        .map(user -> {
          String resetToken = UUID.randomUUID().toString();
          user.setResetPasswordToken(resetToken);
          user.setTokenCreationDate(LocalDateTime.now());
          user.setTokenExpirationDate(LocalDateTime.now().plusHours(1)); // Token válido por 1 hora
          userRepository.save(user);

          String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + resetToken;
          emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), resetLink);
          return true;
        })
        .orElseThrow(() -> new ResourceNotFoundException("Usuario con correo " + email + " no encontrado."));
  }

  @Override
  @Transactional
  public boolean changePasswordWithToken(String token, PasswordChangeDto passwordChangeDto) {
    if (tokenBlacklist.isTokenBlacklisted(token)) {
      throw new IllegalArgumentException("Token ya utilizado o inválido.");
    }

    return userRepository.findByResetPasswordToken(token)
        .filter(user -> user.getTokenExpirationDate() != null && user.getTokenExpirationDate().isAfter(LocalDateTime.now()))
        .map(user -> {
          if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
          }
          user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
          user.setResetPasswordToken(null); // Invalidar el token de reseteo
          user.setTokenCreationDate(null);
          user.setTokenExpirationDate(null);
          userRepository.save(user);
          tokenBlacklist.addTokenToBlacklist(token); // Añadir a la lista negra
          return true;
        })
        .orElseThrow(() -> new ResourceNotFoundException("Token de recuperación de contraseña inválido o expirado."));
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}