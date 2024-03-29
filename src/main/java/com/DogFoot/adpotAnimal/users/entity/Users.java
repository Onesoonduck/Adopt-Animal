package com.DogFoot.adpotAnimal.users.entity;

import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@NoArgsConstructor
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", unique = true)
    private String userId;

    @NotNull
    @Column(name = "user_name")
    private String userName;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String phoneNumber;

    @NotNull
    @Column(name = "user_role")
    private UsersRole userRole;

    @Builder
    public Users(String userId, String userName, String password, String email, String phoneNumber, UsersRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    public UsersDto toDto() {
        UsersDto usersDto = UsersDto.builder()
            .userId(userId)
            .userName(userName)
            .password(password)
            .email(email)
            .phoneNumber(phoneNumber)
            .userRole(userRole)
            .build();
        return usersDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // 현재 사용자의 권한을 가져옵니다.
        UsersRole currentRole = userRole;

        // 권한을 문자열로 변환합니다. 예를 들어, UsersRole.USER는 "ROLE_USER"로 변환됩니다.
        String authority = currentRole.getAuthority();

        // 문자열 권한을 사용하여 SimpleGrantedAuthority 객체를 생성합니다.
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);

        // 생성된 SimpleGrantedAuthority 객체를 단일 항목의 리스트로 만듭니다.
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        // 권한 리스트를 반환합니다.
        return authorities;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
