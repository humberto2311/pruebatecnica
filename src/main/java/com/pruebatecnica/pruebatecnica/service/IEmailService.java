package com.pruebatecnica.pruebatecnica.service;

/**
 * Interfaz para el servicio de envío de correos electrónicos.
 * Define los métodos necesarios para enviar notificaciones por correo.
 */
public interface IEmailService {

  /**
   * Envía un correo electrónico de activación de cuenta a un usuario.
   *
   * @param to El correo electrónico del destinatario.
   * @param name El nombre completo del destinatario.
   * @param activationLink El enlace de activación personalizado para el usuario.
   */
  void sendActivationEmail(String to, String name, String activationLink);

  /**
   * Envía un correo electrónico para restablecer la contraseña a un usuario.
   *
   * @param to El correo electrónico del destinatario.
   * @param name El nombre completo del destinatario.
   * @param resetLink El enlace de restablecimiento de contraseña personalizado para el usuario.
   */
  void sendPasswordResetEmail(String to, String name, String resetLink);
}