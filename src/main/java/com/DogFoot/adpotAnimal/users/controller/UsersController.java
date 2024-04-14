package com.DogFoot.adpotAnimal.users.controller;

import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.dto.UpdateUsersDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.dto.UsersTableDto;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginDto loginDto,
        HttpServletResponse response) {
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
    @PostMapping("/usersEdit")
    public ResponseEntity<UsersDto> updateUsers(@Valid @RequestBody UpdateUsersDto updateDto,
        HttpServletResponse response) {
        Users users = usersService.getUsers();
        UsersDto updateUsersDto = usersService.update(users.getId(), updateDto, response);
        return ResponseEntity.ok(updateUsersDto);
    }

    // 회원 정보 삭제
    @DeleteMapping("/usersDelete")
    public ResponseEntity deleteUsers(HttpServletResponse response, HttpServletRequest request)
        throws IOException {
        usersService.logout(request, response);
        Users users = usersService.getUsers();
        usersService.deleteUsers(users.getId());
        return ResponseEntity.ok().build();
    }

    // 회원 리스트 조회 - 페이지 네이션
    @GetMapping("/api/usersTable")
    public ResponseEntity<Page<UsersTableDto>> getUserTable(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsersTableDto> usersDtoPage = usersService.getUserTable(pageable);
        return ResponseEntity.ok(usersDtoPage);
    }

    // 회원수 조회
    @GetMapping("/api/userCount")
    public ResponseEntity<Long> getUsersCount(HttpServletResponse response) {
        Long userCount = usersService.getUsersCount();
        return ResponseEntity.ok(userCount);
    }
}
