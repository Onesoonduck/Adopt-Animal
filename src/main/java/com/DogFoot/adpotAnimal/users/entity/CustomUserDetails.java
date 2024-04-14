package com.DogFoot.adpotAnimal.users.entity;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private Users user;

    public CustomUserDetails(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // 현재 사용자의 권한을 가져옵니다.
        UsersRole currentRole = user.getUserRole();

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
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
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

    public Users getUser() {
        return user;
    }

}
