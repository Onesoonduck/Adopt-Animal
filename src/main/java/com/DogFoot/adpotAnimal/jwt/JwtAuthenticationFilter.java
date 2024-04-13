package com.DogFoot.adpotAnimal.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // jwt 토큰 추출
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = jwtTokenProvider.getRefreshToken(request);

        // validate 토큰 유효성 검사
        if(accessToken != null && jwtTokenProvider.validateToken(accessToken)){
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if(refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            // access 토큰이 만료되었다면 해당 refresh 토큰을 검증
            JwtToken token = jwtTokenProvider.refreshGenerateAccessToken(refreshToken);
            jwtTokenProvider.storeTokens(response, token.getAccessToken(), token.getRefreshToken(),
                jwtTokenProvider.getIsAutoLogin(request));
        } else {
            logger.info("Invalid or expired token");
            // refresh 토큰까지 만료되었다면 키 정보를 쿠키와 헤더에서 지워야함
        }
        filterChain.doFilter(request, response);
    }
}
