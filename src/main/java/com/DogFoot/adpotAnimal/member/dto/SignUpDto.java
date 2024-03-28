package com.DogFoot.adpotAnimal.member.dto;

import com.DogFoot.adpotAnimal.member.entity.Member;
import com.DogFoot.adpotAnimal.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpDto {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private MemberRole userRole;

    @Builder
    public SignUpDto(String userId, String userName, String password, String email, String phoneNumber, MemberRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    public Member toEntity(String encodedPassword, MemberRole role) {
        Member member = Member.builder()
            .userId(userId)
            .userName(userName)
            .password(encodedPassword)
            .email(email)
            .phoneNumber(phoneNumber)
            .userRole(role)
            .build();
        return member;
    }
}
