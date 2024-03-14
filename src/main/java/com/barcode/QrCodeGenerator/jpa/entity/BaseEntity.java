package com.barcode.QrCodeGenerator.jpa.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity implements Serializable {
  @Id
  @GeneratedValue
  @UuidGenerator(style = UuidGenerator.Style.AUTO)
  private UUID id;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private OffsetDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "updated_by")
  private String updatedBy;

  @PrePersist
  void onCreate() {
    this.createdAt = OffsetDateTime.now();
    if (this.createdBy == null) {
      this.createdBy = "SYSTEM";
    }
  }

  @PreUpdate
  void onUpdate() {
    this.updatedAt = OffsetDateTime.now();
    if (this.updatedBy == null) {
      this.updatedBy = "SYSTEM";
    }
  }
}
