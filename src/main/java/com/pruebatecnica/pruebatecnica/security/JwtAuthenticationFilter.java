package com.pruebatecnica.pruebatecnica.security;



import com.pruebatecnica.pruebatecnica.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que se ejecuta una vez por cada solicitud para validar el token JWT
 * y establecer el contexto de seguridad en Spring Security.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService; // Servicio para operaciones JWT (validar, extraer email, etc.)
  private final UserDetailsService userDetailsService; // Para cargar los detalles del usuario

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    // 1. Verificar si el token JWT está presente en el encabezado
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response); // Si no hay token o no es Bearer, sigue la cadena de filtros
      return;
    }

    jwt = authHeader.substring(7); // Extrae el token (quita "Bearer ")

    // 2. Extraer el email del token
    userEmail = jwtService.extractUsername(jwt);

    // 3. Si el email no es nulo y no hay una autenticación activa en el contexto de seguridad
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // 4. Cargar los detalles del usuario desde la base de datos o servicio de usuarios
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

      // 5. Validar el token
      if (jwtService.isTokenValid(jwt, userDetails)) {
        // 6. Crear un objeto de autenticación
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null, // La contraseña ya no es necesaria aquí
            userDetails.getAuthorities() // Asigna los roles/permisos del usuario
        );
        // 7. Establecer los detalles de la solicitud (IP, sesión, etc.)
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 8. Establecer la autenticación en el SecurityContextHolder
        // Esto indica a Spring Security que el usuario actual está autenticado
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    // 9. Continuar con la cadena de filtros
    filterChain.doFilter(request, response);
  }
}