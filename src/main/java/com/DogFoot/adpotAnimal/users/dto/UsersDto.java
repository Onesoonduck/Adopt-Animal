package com.DogFoot.adpotAnimal.users.dto;

import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersDto {

    private String userId;
    private String userName;

    private String password;
    private String email;

    private String phoneNumber;
    private UsersRole userRole;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @Builder
    public UsersDto(String userId, String userName, String password, String email, String phoneNumber, UsersRole userRole, LocalDateTime created_at, LocalDateTime updated_at) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
