package com.DogFoot.adpotAnimal.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰을 사용하여 인증과 권한 부여를 처리하는 클래스
 * JWT 토큰의 생성, 복호화, 검증 기능 구현
 */

@Slf4j
@Component
public class JwtTokenProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";  // Header KEY 값
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 14일

    private final Key key;

    // application.yml에서 secret 값을 가져와서 key에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
    * User 정보를 가지고 AccessToken, RefreshToken을 생성
    * AccessToken : 인증된 사용자의 권한 정보와 만료 시간
    * RefreshToken : AccessToken의 갱신 (자동 로그인 유지에 사용)
    */
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        /* Access Token 생성
         *  복호화 하여 인증 벙보를 생성
         */
        Date accessTokenExpiresln = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY,authorities)
            .setExpiration(accessTokenExpiresln)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        // AccessToken을 검증하여 유효성을 확인 Refresh Token 생성
        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return JwtToken.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    // jwt 토큰을 복호화하여 토큰에 들어 있는 정보를 꺼내는 메소드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get(AUTHORITIES_KEY)==null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 가져오기
        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // UserDetails 객체를 만들어 Authentication를 반환
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;    // 토큰이 유효하면 true 반환
        } catch (SecurityException | MalformedJwtException e) { // jwt 토큰이 유효하지 않을 경우
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {   // jwt 토큰이 만료되었을 경우
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {   // 지원하지 않는 jwt 토큰 형식
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {  // jwt 클레임 문자열이 비어있을 경우
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    // Refresh 토큰을 검증하여 유효하다면 새로운 accessToke을 생성하여 반환
    public JwtToken refreshValidateToken(String refreshToken) {
        if(!validateToken(refreshToken)){
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Authentication authentication = getAuthentication(refreshToken);

        return generateToken(authentication);
    }

    // Access 토큰을 복호화하여 토큰에 포함된 클레임을 반환
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
