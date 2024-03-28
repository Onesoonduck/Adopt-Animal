package com.DogFoot.adpotAnimal.member.entity;

import com.DogFoot.adpotAnimal.member.dto.MemberDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Getter
@NoArgsConstructor(force = true)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", unique = true)
    private final String userId;

    @NotNull
    @Column(name = "user_name")
    private final String userName;

    @NotNull
    private final String password;

    @NotNull
    @Column(unique = true)
    private final String email;

    @NotNull
    private final String phoneNumber;

    @NotNull
    @Column(name = "user_role")
    private final MemberRole userRole;

    @Builder
    public Member(String userId, String userName, String password, String email, String phoneNumber, MemberRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    public MemberDto toDto() {
        MemberDto memberDto = MemberDto.builder()
            .userId(userId)
            .userName(userName)
            .password(password)
            .email(email)
            .phoneNumber(phoneNumber)
            .userRole(userRole)
            .build();
        return memberDto;
    }


    // todo : getAuthorities 메소드 작성
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.getAuthority()));
    }
}
