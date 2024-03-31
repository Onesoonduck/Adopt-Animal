package com.DogFoot.adpotAnimal;

import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        // 패스워드 암호화
        SignUpDto signUpDto = SignUpDto.builder()
            .userName("won")
            .userId("elice")
            .email("elice@example.com")
            .password("1234")
            .phoneNumber("01012341234")
            .build();
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        // 멤버 리포지터리에 저장
        usersRepository.save(signUpDto.toEntity(encodedPassword, UsersRole.USER));
    }
}
