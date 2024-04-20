package com.barcode.QrCodeGenerator.controller.auth;

import com.barcode.QrCodeGenerator.domain.request.LoginRequest;
import com.barcode.QrCodeGenerator.domain.request.RegistrationRequest;
import com.barcode.QrCodeGenerator.domain.response.LoginResponse;
import com.barcode.QrCodeGenerator.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController implements AuthApi {
  private final AuthService authService;

  @Override
  public ResponseEntity<Void> registerUser(RegistrationRequest registrationRequest) {
    authService.register(registrationRequest);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> registrationRequestAgent(RegistrationRequest registrationRequest) {
    return null;
  }

  @Override
  public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.login(loginRequest));
  }

  @Override
  public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    authService.generateRefreshToken(request, response);
    return ResponseEntity.ok().build();
  }
}
