package com.pruebatecnica.pruebatecnica.service;



import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendActivationEmail(String to, String name, String activationLink) {
    SimpleMailMessage message = new SimpleMailMessage();
    // El remitente debe coincidir con spring.mail.username
    message.setFrom("p4079044@gmail.com");
    message.setTo(to);
    message.setSubject("Activa tu cuenta en Sistema de Usuarios");
    message.setText(
        "Hola " + name + ",\n\n" +
            "Gracias por registrarte. Por favor, haz clic en el siguiente enlace para activar tu cuenta:\n" +
            activationLink + "\n\n" +
            "Este enlace expirará en 24 horas.\n\n" +
            "Saludos,\n" +
            "El equipo de Sistema de Usuarios"
    );
    try {
      mailSender.send(message);
    } catch (MailException e) {
      System.err.println("Error al enviar correo de activación a " + to + ": " + e.getMessage());
      throw new RuntimeException("Error al enviar correo de activación.", e);
    }
  }

  @Override
  public void sendPasswordResetEmail(String to, String name, String resetLink) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("api@mailtrap.io");
    message.setTo(to);
    message.setSubject("Recuperación de Contraseña - Sistema de Usuarios");
    message.setText(
        "Hola " + name + ",\n\n" +
            "Hemos recibido una solicitud para restablecer tu contraseña. Haz clic en el siguiente enlace para establecer una nueva contraseña:\n" +
            resetLink + "\n\n" +
            "Este enlace expirará en 1 hora.\n\n" +
            "Si no solicitaste este cambio, por favor ignora este correo.\n\n" +
            "Saludos,\n" +
            "El equipo de Sistema de Usuarios"
    );
    try {
      mailSender.send(message);
    } catch (MailException e) {
      System.err.println("Error al enviar correo de recuperación a " + to + ": " + e.getMessage());
      throw new RuntimeException("Error al enviar correo de recuperación.", e);
    }
  }
}
