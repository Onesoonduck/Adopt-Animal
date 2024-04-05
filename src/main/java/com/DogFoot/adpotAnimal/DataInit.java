package com.DogFoot.adpotAnimal;

import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UsersService usersService;

    @PostConstruct
    public void init() {
        SignUpDto user = SignUpDto.builder()
            .userId("eliceuser")
            .userName("eliceuser")
            .password("Eliceuser1234!")
            .email("eliceuser1234@example.com")
            .phoneNumber("01012345678")
            .userRole(UsersRole.USER)
            .build();

        SignUpDto admin = SignUpDto.builder()
            .userId("eliceadmin")
            .userName("eliceadmin")
            .password("Eliceadmin1234!")
            .email("eliceadmin1234@example.com")
            .phoneNumber("01023456789")
            .userRole(UsersRole.ADMIN)
            .build();

        usersService.signUp(user);
        usersService.signUp(admin);
    }
}
