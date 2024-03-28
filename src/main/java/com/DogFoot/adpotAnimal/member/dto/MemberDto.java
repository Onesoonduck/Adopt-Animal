package com.DogFoot.adpotAnimal.member.dto;

import com.DogFoot.adpotAnimal.member.entity.MemberRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberDto {

    private String userId;
    private String userName;
    private String password;
    private String email;

    @NotNull(message = "입력해 주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
    private String phoneNumber;
    private MemberRole userRole;

    @Builder
    public MemberDto(String userId, String userName, String password, String email, String phoneNumber, MemberRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }
}
