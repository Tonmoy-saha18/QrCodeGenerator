package com.barcode.QrCodeGenerator.jpa.entity;

import com.barcode.QrCodeGenerator.jpa.enumtype.UserRoleType;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = EntityConstant.USER_TABLE_NAME)
public class UserEntity extends BaseEntity {
  @Column(nullable = false)
  private String firstName;

  private String lastName;

  @Column(nullable = false, unique = true)
  private String userName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(unique = true, nullable = false)
  private String phoneNumber;

  private String password;

  private String primaryAddress;
  private String secondaryAddress;

  @Enumerated(EnumType.STRING)
  private UserRoleType userRoleType;

  @OneToMany(mappedBy = "user")
  private List<TokenEntity> tokenEntities;
}
