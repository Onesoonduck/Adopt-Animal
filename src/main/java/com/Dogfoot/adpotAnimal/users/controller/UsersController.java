package com.DogFoot.adpotAnimal.users.controller;

import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.dto.UpdateUsersDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<JwtToken> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        return ResponseEntity.ok(usersService.login(loginDto, response));
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<UsersDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        UsersDto savedUsersDto = usersService.signUp(signUpDto);
        return ResponseEntity.ok(savedUsersDto);
    }

    // 로그 아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return usersService.logout(request, response);
    }

    // 회원 정보 수정
    @PostMapping("/{id}")
    public ResponseEntity<UsersDto> updateUsers(@PathVariable Long id, @Valid @RequestBody UpdateUsersDto updateDto,  HttpServletResponse response) {
        UsersDto updateUsersDto = usersService.update(id, updateDto, response);
        return ResponseEntity.ok(updateUsersDto);
    }

    // 회원 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUsers(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        usersService.logout(request, response);
        response.sendRedirect("http://localhost:8080/login");
        usersService.deleteUsers(id);
        return ResponseEntity.ok().build();
    }

}
