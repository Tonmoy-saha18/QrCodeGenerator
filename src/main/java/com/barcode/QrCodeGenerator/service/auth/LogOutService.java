package com.barcode.QrCodeGenerator.service.auth;

import com.barcode.QrCodeGenerator.jpa.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogOutService implements LogoutHandler {
  private final TokenRepository tokenRepository;

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    final String jwtToken = authHeader.substring(7);
    tokenRepository
        .findByToken(jwtToken)
        .ifPresent(
            token -> {
              token.setRevoked(true).setExpired(true);
              tokenRepository.save(token);
            });
  }
}
