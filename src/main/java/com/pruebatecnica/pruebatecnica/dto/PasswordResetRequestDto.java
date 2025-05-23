package com.pruebatecnica.pruebatecnica.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequestDto {

  @NotBlank(message = "El correo electrónico no puede estar vacío.")
  @Email(message = "Formato de correo electrónico inválido.")
  private String email;
}
