package com.DogFoot.adpotAnimal.users.entity;

import com.DogFoot.adpotAnimal.users.dto.UsersDto;
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
@NoArgsConstructor
public class Users {

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

    // todo : getAuthorities 메소드 작성
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.getAuthority()));
    }
}
