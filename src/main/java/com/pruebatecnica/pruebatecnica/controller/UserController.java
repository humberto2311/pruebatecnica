package com.pruebatecnica.pruebatecnica.controller;



// UserController.java

import com.pruebatecnica.pruebatecnica.dto.UserRegistrationDto;
import com.pruebatecnica.pruebatecnica.model.User;
import com.pruebatecnica.pruebatecnica.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final IUserService userService;

  public UserController(IUserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario y envía un correo de activación")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Correo electrónico ya registrado")
  })
  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
    User registeredUser = userService.registerUser(registrationDto);
    return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
  }

  @Operation(summary = "Activar cuenta de usuario", description = "Activa la cuenta del usuario utilizando un token de registro")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cuenta activada exitosamente"),
      @ApiResponse(responseCode = "400", description = "Token inválido o expirado"),
      @ApiResponse(responseCode = "404", description = "Token no encontrado")
  })
  @GetMapping("/activate")
  public ResponseEntity<String> activateUser(@RequestParam String token) {
    boolean activated = userService.activateUser(token);
    if (activated) {
      return ResponseEntity.ok("Cuenta activada exitosamente.");
    } else {
      return ResponseEntity.badRequest().body("Token de activación inválido o expirado.");
    }
  }
}