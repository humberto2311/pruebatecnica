package com.pruebatecnica.pruebatecnica.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDto {

  @NotBlank(message = "El correo electrónico no puede estar vacío.")
  @Email(message = "Formato de correo electrónico inválido.")
  private String email;

  @NotBlank(message = "La contraseña no puede estar vacía.")
  @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.") // O el tamaño mínimo que decidas
  private String password;
}
