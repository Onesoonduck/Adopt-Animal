package com.DogFoot.adpotAnimal.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum MemberRole implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
