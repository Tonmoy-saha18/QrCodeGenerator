package com.barcode.QrCodeGenerator.controller.auth;

import com.barcode.QrCodeGenerator.domain.request.LoginRequest;
import com.barcode.QrCodeGenerator.domain.request.RegistrationRequest;
import com.barcode.QrCodeGenerator.domain.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth")
@RequestMapping(path = "api/auth")
public interface AuthApi {
  @Operation(description = "Register a user")
  @PostMapping(path = "register")
  ResponseEntity<Void> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest);

  @Operation(description = "Login an existing user")
  @PostMapping(path = "login")
  ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest);

  @Operation(description = "Generate refresh token")
  @PostMapping(path = "refresh-token")
  ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException;
}
