package com.DogFoot.adpotAnimal;

import com.DogFoot.adpotAnimal.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {

    }
}
