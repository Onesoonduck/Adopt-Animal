package com.DogFoot.adpotAnimal.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class JwtToken {
    private String grantType;       // jwt에 대한 인증 타입
    private String accessToken;
    private String refreshToken;
}
