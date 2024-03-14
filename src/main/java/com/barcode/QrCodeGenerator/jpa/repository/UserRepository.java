package com.barcode.QrCodeGenerator.jpa.repository;

import com.barcode.QrCodeGenerator.jpa.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  @Query(
      value = "select u from UserEntity u where  u.userName=:username or u.phoneNumber=:username")
  Optional<UserEntity> findByUserNameOrPhoneNumber(String username);
}
