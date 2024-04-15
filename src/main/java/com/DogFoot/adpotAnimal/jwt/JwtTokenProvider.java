package com.DogFoot.adpotAnimal.jwt;

import com.DogFoot.adpotAnimal.tokenBlack.TokenBlackService;
import com.DogFoot.adpotAnimal.users.entity.CustomUserDetails;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * JWT 토큰을 사용하여 인증과 권한 부여를 처리하는 클래스
 * JWT 토큰의 생성, 복호화, 검증 기능 구현
 */

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;          // 30분

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 14일

    private final Key key;

    private final UsersRepository usersRepository;
    private TokenBlackService tokenBlackService;
    // application.yml에서 secret 값을 가져와서 key에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, UsersRepository usersRepository, TokenBlackService tokenBlackService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.usersRepository = usersRepository;
        this.tokenBlackService = tokenBlackService;
    }

    /**
    * User 정보를 가지고 AccessToken, RefreshToken을 생성
    * AccessToken : 인증된 사용자의 권한 정보와 만료 시간
    * RefreshToken : AccessToken의 갱신 (자동 로그인 유지에 사용)
    */
    public JwtToken generateToken(Authentication authentication) {

        // AccessToken 생성
        String accessToken = generateAccessToken(authentication);

        // AccessToken을 검증하여 Refresh Token 생성
        String refreshToken = generateRefreshToken();

        return JwtToken.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    // 액세스 토큰을 생성
    private String generateAccessToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date accessTokenExpiresln = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY,authorities)
            .setExpiration(accessTokenExpiresln)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return accessToken;
    }

    // Refresh 토큰을 검증하여 유효하다면 새로운 accessToke을 생성하여 반환
    public JwtToken refreshGenerateAccessToken(String refreshToken) {
        if(!validateToken(refreshToken)){
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Authentication authentication = getAuthentication(refreshToken);
        String accessToken = generateAccessToken(authentication);

        // RefreshToken을 검증하여 만료기한이 30분 이내라면 refreshToken 도 같이 발급
        Date expirationDate = getExpirationDate(refreshToken);
        if (TimeUnit.MILLISECONDS.toMinutes(expirationDate.getTime() - System.currentTimeMillis()) <= 30) {
            refreshToken = generateRefreshToken();
        }

        return JwtToken.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    // 리프레쉬 토큰을 생성
    private String generateRefreshToken() {
        long now = (new Date()).getTime();
        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
        return refreshToken;
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
        CustomUserDetails principal = new CustomUserDetails(usersRepository.findByUserId(
            claims.getSubject()).get());
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {

        if(tokenBlackService.existsToken(token)){
            return false;
        }

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

    // Request Header에서 access 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 쿠키에서 Refresh 토큰 추출
    public String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 쿠키에서 자동 로그인 여부 추출
    public boolean getIsAutoLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AUTOLOGIN".equals(cookie.getName())) {
                    return cookie.getValue().equals("TRUE");
                }
            }
        }
        return false;
    }

    // 토큰의 만료시간 획득
    public Date getExpirationDate(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration();
    }

    // accessToken은 헤더, refreshToken은 쿠키에 저장
    public void storeTokens(HttpServletResponse response, String accessToken, String refreshToken, boolean isAutoLogin) {
        // 헤더에 accessToken 저장
        response.setHeader("Authorization", "Bearer " + accessToken);

        // 쿠키에 refreshToken 저장
        Cookie cookie = new Cookie("TOKEN", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        // 자동 로그인 시 max-age 속성을 설정하여 브라우저 종료 시에도 쿠키가 사라지지 않도록 함
        if(isAutoLogin){
            cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(REFRESH_TOKEN_EXPIRE_TIME));
            response.addCookie(cookie);
            cookie = new Cookie("AUTOLOGIN", "TRUE");
            cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(REFRESH_TOKEN_EXPIRE_TIME));
            response.addCookie(cookie);
        } else {
            cookie = new Cookie("AUTOLOGIN", "FALSE");
            response.addCookie(cookie);
        }
    }

    public void deleteStoreTokens(HttpServletResponse response) {
        // 헤더에서 토큰 삭제
        response.setHeader("Authorization", "");

        // 쿠키에서 토큰 삭제
        Cookie cookie = new Cookie("TOKEN", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        cookie = new Cookie("AUTOLOGIN", "FALSE");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
