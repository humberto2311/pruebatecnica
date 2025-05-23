package com.pruebatecnica.pruebatecnica.respository;

import com.pruebatecnica.pruebatecnica.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  Optional<User> findByRegistrationToken(String registrationToken);
  Optional<User> findByResetPasswordToken(String resetPasswordToken);
}