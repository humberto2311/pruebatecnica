package com.pruebatecnica.pruebatecnica.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDto {

  @NotBlank(message = "La nueva contraseña no puede estar vacía.")
  @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres.") // O el tamaño mínimo que decidas
  private String newPassword;

  @NotBlank(message = "La confirmación de la nueva contraseña no puede estar vacía.")
  private String confirmNewPassword;
}