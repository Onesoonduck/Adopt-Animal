package com.DogFoot.adpotAnimal.users.service;

import com.DogFoot.adpotAnimal.tokenBlack.TokenBlack.TokenType;
import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.jwt.JwtTokenProvider;
import com.DogFoot.adpotAnimal.tokenBlack.TokenBlackService;
import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.entity.CustomUserDetails;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlackService tokenBlackService;
    @Transactional
    public JwtToken login(LoginDto loginDto, HttpServletResponse response) {
        String userId = loginDto.getUserId();
        String password = loginDto.getPassword();

        // userId, password를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password, null);

        // 요청된 Member에 대한 검증 진행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("아이디나 비밀번호가 잘못되었습니다.");
        }

        // 인증 정보를 기반으로 jwt 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        jwtTokenProvider.storeTokens(response, jwtToken.getAccessToken(),
            jwtToken.getRefreshToken(), false);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();

        return jwtToken;
    }

    @Transactional
    public UsersDto sighUp(SignUpDto signUpDto) {

        // 유효성 체크
        if (usersRepository.existsByUserId(signUpDto.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        } else if (usersRepository.existsByEmail(signUpDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        // 멤버 리포지터리에 저장
        Users signUsers = usersRepository.save(
            signUpDto.toEntity(encodedPassword, UsersRole.USER));

        // 저장된 멤버 엔티티를 dto로 변환
        return signUsers.toDto();
    }

    @Transactional
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 헤더에서 accessToken을 추출하여 유효한 지 검증하고 블랙리스트에 저장한다.
        String accessToken = jwtTokenProvider.resolveToken(request);
        if(jwtTokenProvider.validateToken(accessToken)){
            Instant expiredTimeInstant = jwtTokenProvider.getExpirationDate(accessToken).toInstant();
            LocalDateTime expiredTime = expiredTimeInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            tokenBlackService.saveTokenToBlacklist(accessToken, TokenType.ACCESS, expiredTime);
        }

        // 쿠키에서 refresh 토큰을 추출하여 유효한 지 검증하여 블랙리스트에 저장한다.
        String refreshToken = jwtTokenProvider.getRefreshToken(request);
        if(jwtTokenProvider.validateToken(refreshToken)){

            Instant expiredTimeInstant = jwtTokenProvider.getExpirationDate(refreshToken).toInstant();
            LocalDateTime expiredTime = expiredTimeInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            tokenBlackService.saveTokenToBlacklist(accessToken, TokenType.REFRESH, expiredTime);
        }

        // 헤더와 쿠키에서 토큰 삭제
        jwtTokenProvider.deleteStoreTokens(response);

        return ResponseEntity.ok("로그아웃 완료");
    }
}
