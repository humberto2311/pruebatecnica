package com.pruebatecnica.pruebatecnica.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


  @Value("${jwt.secret}")
  private String secretKey;


  @Value("${jwt.expiration}")
  private long jwtExpiration;

  /**
   * Extrae el email (sujeto) del token JWT.
   * @param token El token JWT.
   * @return El email del usuario.
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extrae una declaración específica del token JWT.
   * @param token El token JWT.
   * @param claimsResolver Función para resolver la declaración.
   * @param <T> Tipo de la declaración.
   * @return El valor de la declaración.
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Genera un token JWT para un usuario.
   * @param userDetails Detalles del usuario.
   * @return El token JWT generado.
   */
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  /**
   * Genera un token JWT con declaraciones adicionales.
   * @param extraClaims Declaraciones adicionales a incluir.
   * @param userDetails Detalles del usuario.
   * @return El token JWT generado.
   */
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Fecha de expiración
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Valida si un token JWT es válido para un usuario dado.
   * @param token El token JWT.
   * @param userDetails Detalles del usuario.
   * @return true si el token es válido, false en caso contrario.
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    // Utiliza una lambda para comprobar la validez.
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  /**
   * Verifica si un token JWT ha expirado.
   * @param token El token JWT.
   * @return true si el token ha expirado, false en caso contrario.
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extrae la fecha de expiración del token.
   * @param token El token JWT.
   * @return La fecha de expiración.
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extrae todas las declaraciones (claims) del token.
   * @param token El token JWT.
   * @return Las declaraciones.
   */
  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Obtiene la clave de firma desde la clave secreta.
   * @return La clave de firma.
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}