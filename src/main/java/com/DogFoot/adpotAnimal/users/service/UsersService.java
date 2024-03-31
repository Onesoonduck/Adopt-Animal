package com.DogFoot.adpotAnimal.users.service;

import com.DogFoot.adpotAnimal.tokenBlack.TokenBlack;
import com.DogFoot.adpotAnimal.tokenBlack.TokenBlackRepository;
import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.jwt.JwtTokenProvider;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final TokenBlackRepository tokenBlackRepository;

    @Transactional
    public JwtToken login(String userId, String password) {

        // userId, password를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);

        // 요청된 Member에 대한 검증 진행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("아이디나 비밀번호가 잘못되었습니다.");
        }

        // 인증 정보를 기반으로 jwt 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
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
    public ResponseEntity logout(String token) {

        // 토큰이 유효한 지 검증
        if(!jwtTokenProvider.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // 멤버의 정보를 확인
        jwtTokenProvider.getAuthentication(token);

        // 토큰 블랙리스트에 추가
        tokenBlackRepository.save(new TokenBlack(token));

        return ResponseEntity.ok("로그아웃 완료");
    }

    // Refresh 토큰을 검증하여 새로운 토큰을 발급
    public JwtToken revalidatedToken(String token) {
        if(tokenBlackRepository.existsByToken(token)){
            throw new IllegalArgumentException("토큰이 만료되었습니다.");
        }

        return jwtTokenProvider.refreshValidateToken(token);
    }
}
