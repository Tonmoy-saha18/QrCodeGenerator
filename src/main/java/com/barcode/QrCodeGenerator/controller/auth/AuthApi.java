package com.barcode.QrCodeGenerator.controller.auth;

import com.barcode.QrCodeGenerator.domain.request.LoginRequest;
import com.barcode.QrCodeGenerator.domain.request.RegistrationRequest;
import com.barcode.QrCodeGenerator.domain.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth")
@RequestMapping(path = "api/auth")
public interface AuthApi {
  @Operation(description = "Register a consumer user")
  @PostMapping(path = "consumer-user/register")
  ResponseEntity<Void> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest);

  @Operation(description = "Registration request of a agent")
  @PostMapping(path = "agent/register")
  ResponseEntity<Void> registrationRequestAgent(
      @RequestBody @Valid RegistrationRequest registrationRequest);

  @Operation(description = "Login an existing user")
  @PostMapping(path = "login")
  ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest);
}
