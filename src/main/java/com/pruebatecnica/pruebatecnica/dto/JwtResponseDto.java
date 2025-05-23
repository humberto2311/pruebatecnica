package com.pruebatecnica.pruebatecnica.dto;


import lombok.AllArgsConstructor; // De Lombok
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
  private String token;
  private String type = "Bearer";


   private Long userId;
   private String email;

  public JwtResponseDto(String token) {
    this.token = token;
  }
}