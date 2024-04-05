package com.DogFoot.adpotAnimal.Member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("비밀번호 테스트")
@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void pwdMatchTest(){
        // 기존 저장해두었던 암호화된 비밀번호
        String encodedPwd = "{bcrypt}$2a$10$eR79CUYwnz9Kbgcmv3T/leyLaVOeMTq3d8G5bSOM6Q.J7dCv59sQa";
        // 검증할 비밀번호
        String newPwd = "@abcd12345678";
        System.out.println(passwordEncoder.encode(newPwd));

        if(passwordEncoder.matches(newPwd, encodedPwd)){
            System.out.println("true");
        }else{
            System.out.println("false");
        }
    }
}
