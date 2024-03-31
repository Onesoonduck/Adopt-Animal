package com.DogFoot.adpotAnimal.users.controller;

import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.jwt.JwtTokenProvider;
import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
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
    @PostMapping("/login")
    public JwtToken login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        String userId = loginDto.getUserId();
        String password = loginDto.getPassword();
        JwtToken jwtToken = usersService.login(userId, password);
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, jwtToken.getRefreshToken());

        // 쿠키 생성 및 쿠키 세팅
        Cookie cookie = new Cookie("TOKEN", jwtToken.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        // 자동 로그인 시 max-age 속성을 설정하여 브라우저 종료 시에도 쿠키가 사라지지 않도록 함
        if(false){
            cookie.setMaxAge(1000 * 60 * 60 * 24 * 14);
        }

        response.addCookie(cookie);

        return jwtToken;
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<UsersDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        UsersDto savedUsersDto = usersService.sighUp(signUpDto);
        return ResponseEntity.ok(savedUsersDto);
    }


    // 로그 아웃
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        String token = Arrays.stream(cookies)
            .filter(cookie -> "TOKEN".equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);

        return usersService.logout(token);
    }

}
