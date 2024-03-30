package com.DogFoot.adpotAnimal.TokenBlacklist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlackRepository extends JpaRepository<TokenBlack, Long> {
    boolean existByTokens(String token);
}
