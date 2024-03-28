package com.DogFoot.adpotAnimal.users.controller;

import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.dto.SignInDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    // 로그인
    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
        String userId = signInDto.getUserId();
        String password = signInDto.getPassword();
        JwtToken jwtToken = usersService.signIn(userId, password);

        // 확인
        log.info("request userId = {}, password = {}", userId, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    // 회원 가입
    @PostMapping("/sign-up")
    public ResponseEntity<UsersDto> signUp(@RequestBody SignUpDto signUpDto) {
        UsersDto savedUsersDto = usersService.sighUp(signUpDto);
        return ResponseEntity.ok(savedUsersDto);
    }

    // 테스트용
    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
