package com.pruebatecnica.pruebatecnica.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data; // De Lombok

@Data // Genera getters, setters, toString, equals y hashCode
public class UserRegistrationDto {

  @NotBlank(message = "El nombre completo no puede estar vacío.")
  @Size(min = 3, max = 100, message = "El nombre completo debe tener entre 3 y 100 caracteres.")
  private String fullName;

  @NotBlank(message = "El correo electrónico no puede estar vacío.")
  @Email(message = "Formato de correo electrónico inválido.")
  @Size(max = 100, message = "El correo electrónico no puede exceder los 100 caracteres.")
  private String email;


  @NotBlank(message = "La contraseña no puede estar vacía.")
  @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres.")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$",
      message = "La contraseña debe contener al menos una letra y un número")
  private String password;
}