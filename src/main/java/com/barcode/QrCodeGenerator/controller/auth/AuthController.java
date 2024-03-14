package com.barcode.QrCodeGenerator.controller.auth;

import com.barcode.QrCodeGenerator.domain.request.LoginRequest;
import com.barcode.QrCodeGenerator.domain.request.RegistrationRequest;
import com.barcode.QrCodeGenerator.domain.response.LoginResponse;
import com.barcode.QrCodeGenerator.service.auth.AuthService;
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
  public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.login(loginRequest));
  }
}
