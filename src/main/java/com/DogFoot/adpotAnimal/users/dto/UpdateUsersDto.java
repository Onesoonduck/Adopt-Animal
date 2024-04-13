package com.DogFoot.adpotAnimal.users.dto;

import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUsersDto {

    private String userId;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private UsersRole userRole;

    @Builder
    public UpdateUsersDto(String userId, String userName, String password, String email,
        String phoneNumber, UsersRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    public Users toEntity(String usersId, String encodedPassword, UsersRole role) {
        Users users = Users.builder()
            .userId(usersId)
            .userName(userName)
            .password(encodedPassword)
            .email(email)
            .phoneNumber(phoneNumber)
            .userRole(role)
            .build();
        return users;
    }
}
