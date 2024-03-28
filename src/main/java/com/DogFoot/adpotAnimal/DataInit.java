package com.DogFoot.adpotAnimal;

import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final UsersRepository usersRepository;

    @PostConstruct
    public void init() {

    }
}
