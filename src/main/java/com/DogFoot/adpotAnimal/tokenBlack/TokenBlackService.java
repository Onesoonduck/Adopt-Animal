package com.DogFoot.adpotAnimal.tokenBlack;

import com.DogFoot.adpotAnimal.tokenBlack.TokenBlack.TokenType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlackService {

    private final TokenBlackRepository tokenBlackRepository;

    public boolean existsToken(String token) {
        return tokenBlackRepository.existsByToken(token);
    }

    public void saveTokenToBlacklist(String token, TokenType tokenType, LocalDateTime expiredTime) {
        tokenBlackRepository.save(new TokenBlack(token, tokenType, expiredTime));
    }

    @Async
    @Scheduled(fixedRate = 60000)
    public void deleteExpiredTokens() {
        tokenBlackRepository.deleteExpiredTokens();
    }
}
