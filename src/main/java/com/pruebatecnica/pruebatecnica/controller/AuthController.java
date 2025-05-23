package com.pruebatecnica.pruebatecnica.controller;



import com.pruebatecnica.pruebatecnica.dto.JwtResponseDto;
import com.pruebatecnica.pruebatecnica.dto.LoginRequestDto;
import com.pruebatecnica.pruebatecnica.dto.PasswordChangeDto;
import com.pruebatecnica.pruebatecnica.dto.PasswordResetRequestDto;

import com.pruebatecnica.pruebatecnica.service.IAuthService;
import com.pruebatecnica.pruebatecnica.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final IAuthService authService;
  private final IUserService userService; // Para recuperación de contraseña

  public AuthController(IAuthService authService, IUserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  @Operation(summary = "Inicio de sesión de usuario", description = "Valida credenciales y retorna un token JWT")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
      @ApiResponse(responseCode = "401", description = "Credenciales inválidas o usuario inactivo")
  })
  @PostMapping("/login")
  public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
    String jwt = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
    return ResponseEntity.ok(new JwtResponseDto(jwt));
  }

  @Operation(summary = "Solicitar recuperación de contraseña", description = "Envía un token de recuperación al correo electrónico del usuario")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Correo de recuperación enviado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @PostMapping("/forgot-password")
  public ResponseEntity<String> forgotPassword(@Valid @RequestBody PasswordResetRequestDto request) {
    userService.resetPassword(request.getEmail());
    return ResponseEntity.ok("Se ha enviado un enlace de recuperación de contraseña a su correo electrónico.");
  }

  @Operation(summary = "Cambiar contraseña usando un token", description = "Permite al usuario establecer una nueva contraseña utilizando un token de recuperación")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
      @ApiResponse(responseCode = "400", description = "Token inválido o expirado / Contraseñas no coinciden"),
      @ApiResponse(responseCode = "404", description = "Token no encontrado")
  })
  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(@RequestParam String token, @Valid @RequestBody PasswordChangeDto passwordChangeDto) {
    userService.changePasswordWithToken(token, passwordChangeDto);
    return ResponseEntity.ok("Su contraseña ha sido restablecida exitosamente.");
  }
}