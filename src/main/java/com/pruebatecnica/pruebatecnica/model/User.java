package com.pruebatecnica.pruebatecnica.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data; // De Lombok
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String fullName;

  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)

  private String password; // hashed password

  private String registrationToken;
  private String resetPasswordToken;

  private boolean active = false;

  @Column(name = "token_creation_date")
  private LocalDateTime tokenCreationDate;

  @Column(name = "token_expiration_date")
  private LocalDateTime tokenExpirationDate;
}