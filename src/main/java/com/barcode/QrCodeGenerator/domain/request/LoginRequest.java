package com.barcode.QrCodeGenerator.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
  @NotBlank(message = "Username can't be empty")
  private String userName;

  @NotBlank(message = "Password can't be empty")
  @Min(value = 6, message = "password must be six digit long")
  private String password;
}
