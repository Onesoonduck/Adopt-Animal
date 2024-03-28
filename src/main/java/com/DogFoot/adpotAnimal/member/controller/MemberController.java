package com.DogFoot.adpotAnimal.member.controller;

import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.member.dto.MemberDto;
import com.DogFoot.adpotAnimal.member.dto.SignInDto;
import com.DogFoot.adpotAnimal.member.dto.SignUpDto;
import com.DogFoot.adpotAnimal.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인
    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
        String userId = signInDto.getUserId();
        String password = signInDto.getPassword();
        JwtToken jwtToken = memberService.signIn(userId, password);

        // 확인
        log.info("request userId = {}, password = {}", userId, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    // 회원 가입
    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto) {
        MemberDto savedMemberDto = memberService.sighUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }

    // 테스트용
    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
