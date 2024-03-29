package com.DogFoot.adpotAnimal.users.service;

import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.jwt.JwtTokenProvider;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    //  로그인 요청으로 들어온 아이디와 패스워드를 검증하여 jwt 토큰 생성
    @Transactional
    public JwtToken login(String userId, String password) {

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

}
