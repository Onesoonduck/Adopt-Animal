package com.DogFoot.adpotAnimal.users.controller;

import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.jwt.JwtTokenProvider;
import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, jwtToken.getAccessToken());
        return jwtToken;
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<UsersDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        UsersDto savedUsersDto = usersService.sighUp(signUpDto);
        return ResponseEntity.ok(savedUsersDto);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenProvider.AUTHORIZATION_HEADER);
        return usersService.logout(token);
    }

}
