package com.barcode.QrCodeGenerator.jpa.repository;

import com.barcode.QrCodeGenerator.jpa.entity.TokenEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {
  @Query(
      """
				select t from TokenEntity t inner join UserEntity u on t.user.id = u.id
				where u.id = :userId and (t.expired = false or t.revoked = false)
			""")
  List<TokenEntity> findValidTokenByUser(UUID userId);

  Optional<TokenEntity> findByToken(String token);
}
