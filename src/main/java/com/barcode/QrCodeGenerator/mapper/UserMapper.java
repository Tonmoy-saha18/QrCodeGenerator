package com.barcode.QrCodeGenerator.mapper;

import com.barcode.QrCodeGenerator.domain.request.RegistrationRequest;
import com.barcode.QrCodeGenerator.jpa.entity.UserEntity;

public class UserMapper {
  public static UserEntity domainToEntity(RegistrationRequest request) {
    return new UserEntity()
        .setFirstName(request.getFirstName())
        .setLastName(request.getLastName())
        .setUserName(request.getEmail())
        .setEmail(request.getEmail())
        .setPhoneNumber(request.getPhoneNumber())
        .setPassword(request.getPassword1())
        .setUserRoleType(request.getUserRoleType());
  }
}
