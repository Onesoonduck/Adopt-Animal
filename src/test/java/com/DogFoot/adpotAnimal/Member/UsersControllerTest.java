package com.DogFoot.adpotAnimal.Member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("유저 컨트롤러 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersControllerTest {

    @LocalServerPort
    private int randomPort;

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    TestRestTemplate testRestTemplate;

    @DisplayName("회원가입 테스트")
    @Test
    public void signUpTest() throws Exception {

        SignUpDto signUpDto = SignUpDto.builder()
            .userName("elice track")
            .userId("elice")
            .email("elice@example.com")
            .password("1234")
            .phoneNumber("01012341234")
            .build();

        // Api 요청 설정
        String url = "http://localhost:" + randomPort + "/users/signup";
        ResponseEntity<UsersDto> responseEntity = testRestTemplate.postForEntity(url, signUpDto, UsersDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UsersDto usersDto = responseEntity.getBody();
        assertThat(usersDto.getUserId()).isEqualTo(signUpDto.getUserId());
        assertThat(usersDto.getEmail()).isEqualTo(signUpDto.getEmail());
        assertThat(usersDto.getPhoneNumber()).isEqualTo(signUpDto.getPhoneNumber());
        assertThat(usersDto.getUserName()).isEqualTo(signUpDto.getUserName());

    }

    @DisplayName("로그인 테스트")
    @Test
    public void loginTest() throws Exception {

        LoginDto loginDto = LoginDto.builder()
            .userId("elice")
            .password("1234")
            .build();

        String url = "https://localhost:" + randomPort + "/users/login";

    }

}
