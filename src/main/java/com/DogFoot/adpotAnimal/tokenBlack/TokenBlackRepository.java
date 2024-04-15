package com.DogFoot.adpotAnimal.tokenBlack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenBlackRepository extends JpaRepository<TokenBlack, Long> {
    boolean existsByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM TokenBlack t WHERE t.expiryDate < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
}
