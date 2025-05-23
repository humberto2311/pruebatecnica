package com.pruebatecnica.pruebatecnica.service;


import com.pruebatecnica.pruebatecnica.dto.PasswordChangeDto;
import com.pruebatecnica.pruebatecnica.dto.UserRegistrationDto;
import com.pruebatecnica.pruebatecnica.model.User;
import java.util.Optional;

public interface IUserService {
  User registerUser(UserRegistrationDto registrationDto);
  boolean activateUser(String token);
  boolean resetPassword(String email);
  boolean changePasswordWithToken(String token, PasswordChangeDto passwordChangeDto);
  Optional<User> findUserByEmail(String email);
}