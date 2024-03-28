package com.DogFoot.adpotAnimal.member.dto;

import com.DogFoot.adpotAnimal.member.entity.Users;
import com.DogFoot.adpotAnimal.member.entity.UsersRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpDto {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private UsersRole userRole;

    @Builder
    public SignUpDto(String userId, String userName, String password, String email, String phoneNumber, UsersRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    public Users toEntity(String encodedPassword, UsersRole role) {
        Users users = Users.builder()
            .userId(userId)
            .userName(userName)
            .password(encodedPassword)
            .email(email)
            .phoneNumber(phoneNumber)
            .userRole(role)
            .build();
        return users;
    }
}
