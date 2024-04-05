package com.DogFoot.adpotAnimal.users.dto;

import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersDto {

    private String userId;
    private String userName;

//    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])^[a-zA-Z0-9~!@#$%^&*()+|=]{8,15}$", message = "비밀번호는 최소 8자 이상, 15자 이하이며, 영문과 숫자, 특수문자만 입력하세요.")
    private String password;
    private String email;

//    @Pattern(regexp = "(?=.*[0-9])^[0-9]{11}$", message = "-을 제외한 10자리 번호를 입력해주세요")
    private String phoneNumber;
    private UsersRole userRole;

    @Builder
    public UsersDto(String userId, String userName, String password, String email, String phoneNumber, UsersRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }
}
