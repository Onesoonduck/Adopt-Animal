package com.DogFoot.adpotAnimal.users.service;

import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 사용자 아이디를 기반으로 사용자 정보를 데이터베이스에서 찾습니다.
        Optional<Users> optionalUsers = usersRepository.findByUserId(userId);

        // 사용자 정보가 존재하는 경우
        if (optionalUsers.isPresent()) {
            // 사용자 정보를 가져옵니다.
            Users users = optionalUsers.get();

            // UserDetails 객체를 생성하여 반환합니다.
            return createUserDetails(users);
        } else {
            // 사용자 정보가 존재하지 않는 경우, 예외를 발생시킵니다.
            throw new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다.");
        }
    }

    // 해당하는 유저의 데이터가 존재한다면 UserDetails 객체로 반환
    private UserDetails createUserDetails(Users users) {
        return User.builder()
            .username(users.getUserId())
            .password(users.getPassword())
            .roles(users.getUserRole().toString())
            .build();
    }
}
