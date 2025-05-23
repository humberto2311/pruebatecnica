package com.pruebatecnica.pruebatecnica.service;




/**
 * Interfaz para el servicio de autenticación y manejo de tokens JWT.
 * Define las operaciones relacionadas con el inicio de sesión y la generación de tokens.
 */
public interface IAuthService {

  /**
   * Autentica a un usuario utilizando su correo electrónico y contraseña.
   * Si las credenciales son válidas y el usuario está activo, genera y retorna un token JWT.
   *
   * @param email El correo electrónico del usuario.
   * @param password La contraseña del usuario.
   * @return Un String que representa el token JWT generado.
   * @throws org.springframework.security.core.AuthenticationException Si las credenciales son inválidas o el usuario no está activo.
   */
  String authenticateUser(String email, String password);


}