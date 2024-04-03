package com.DogFoot.adpotAnimal.tokenBlack;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

@Entity
@NoArgsConstructor
@Getter
public class TokenBlack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private TokenType type;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
    public enum TokenType {
        ACCESS,
        REFRESH
    }
    public TokenBlack(String token, TokenType type, LocalDateTime expiryDate) {
        this.token = token;
        this.type = type;
        this.expiryDate = expiryDate;
    }
}
