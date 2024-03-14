package com.barcode.QrCodeGenerator.domain.request;

import com.barcode.QrCodeGenerator.jpa.enumtype.UserRoleType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class RegistrationRequest {
  @NotBlank(message = "First name can't be empty")
  private String firstName;

  private String lastName;

  @Email
  @NotBlank(message = "Email can't be empty")
  private String email;

  @NotBlank(message = "Password can't be empty")
  @Min(value = 6, message = "Password length must be greater than 6")
  private String password1;

  @NotBlank(message = "Password can't be empty")
  @Min(value = 6, message = "Password length must be greater than 6")
  private String password2;

  @NotBlank(message = "Phone number can't be empty")
  @Pattern(regexp = "[0-9]+")
  private String phoneNumber;

  @NotNull private UserRoleType userRoleType;
}
