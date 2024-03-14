package com.barcode.QrCodeGenerator.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {
  private String token;
}
