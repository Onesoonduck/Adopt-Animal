package com.DogFoot.adpotAnimal.member.service;

import com.DogFoot.adpotAnimal.member.entity.Member;
import com.DogFoot.adpotAnimal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return memberRepository.findByUserId(userId)
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }

    // 해당하는 유저의 데이터가 존재한다면 UserDetails 객체로 반환
    private UserDetails createUserDetails(Member member) {
        return User.builder()
            .username(member.getUserId())
            .password(passwordEncoder.encode(member.getPassword()))
            .roles(member.getUserRole().toString())
            .build();
    }
}
