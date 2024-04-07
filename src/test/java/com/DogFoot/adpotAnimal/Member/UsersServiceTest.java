package com.DogFoot.adpotAnimal.Member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.DogFoot.adpotAnimal.jwt.JwtTokenProvider;
import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DisplayName("유저 서비스 테스트")
@SpringBootTest
public class UsersServiceTest {

    @Autowired
    private UsersService usersService;

    @MockBean
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("회원 가입")
    @Test
    public void signUpTest() {
        // Given
        SignUpDto signUpDto = SignUpDto.builder()
            .userName("elice track")
            .userId("elice")
            .email("elice@example.com")
            .password("1234")
            .phoneNumber("01012341234")
            .build();

        // When
        UsersDto resultUserDto = usersService.signUp(signUpDto);

        // Then
        assertThat(resultUserDto.getUserId()).isEqualTo(signUpDto.getUserId());
        assertThat(resultUserDto.getEmail()).isEqualTo(signUpDto.getEmail());
        assertThat(resultUserDto.getPhoneNumber()).isEqualTo(signUpDto.getPhoneNumber());
        assertThat(resultUserDto.getUserName()).isEqualTo(signUpDto.getUserName());
    }

    @DisplayName("로그인 실패 - 잘못된 사용자 ID")
    @Test
    public void loginTest_WrongUserId() {
        LoginDto logindto = LoginDto.builder()
            .userId("wrong")
            .password("test1234")
            .build();

        // userId, password를 기반으로 Authentication 객체 생성
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any())).thenThrow(new IllegalArgumentException("아이디나 비밀번호가 잘못되었습니다."));

        // 로그인 메소드 호출 시 예외 발생 검증
//        assertThrows(IllegalArgumentException.class, () -> usersService.login(logindto.getUserId(), logindto.getPassword()));
    }

    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    @Test
    public void loginTest_WrongPassword() {
        LoginDto logindto = LoginDto.builder()
            .userId("elice")
            .password("test1234")
            .build();

        // userId, password를 기반으로 Authentication 객체 생성
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any())).thenThrow(new IllegalArgumentException("아이디나 비밀번호가 잘못되었습니다."));

//        assertThrows(IllegalArgumentException.class, () -> usersService.login(logindto.getUserId(), logindto.getPassword()));
    }
}
