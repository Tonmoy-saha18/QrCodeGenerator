package com.barcode.QrCodeGenerator.jpa.entity;

import com.barcode.QrCodeGenerator.jpa.enumtype.TokenType;
import jakarta.persistence.*;
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
@Entity
@Table(name = EntityConstant.TOKEN_TABLE_NAME)
public class TokenEntity extends BaseEntity {
  private String token;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType;

  private boolean expired;
  private boolean revoked;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
}
