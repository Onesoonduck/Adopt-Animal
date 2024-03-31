package com.DogFoot.adpotAnimal.tokenBlack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlackRepository extends JpaRepository<TokenBlack, Long> {
    boolean existsByToken(String token);

    // TODO : 토큰이 저장될 때 남은 유효기간을 확인하여 해당 기간이 지나면 토큰을 제거하는 메소드
}
