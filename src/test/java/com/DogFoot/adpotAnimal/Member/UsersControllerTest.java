package com.DogFoot.adpotAnimal.Member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.DogFoot.adpotAnimal.users.controller.UsersController;
import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.dto.UpdateUsersDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@DisplayName("유저 컨트롤러 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersControllerTest {

    @LocalServerPort
    private int randomPort;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    UsersController usersController;

    // 각각의 테스트 메서드가 실행되기 전 MockMvc 객체 초기화
    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @DisplayName("회원가입 테스트")
    @Test
    public void signUpTest() throws Exception {

        SignUpDto signUpDto = SignUpDto.builder()
            .userName("elice track")
            .userId("elice")
            .email("elice@example.com")
            .password("@abcd12345678")
            .phoneNumber("01012341234")
            .userRole(UsersRole.USER)
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
            .password("@abcd12345678")
            .build();

        String url = "https://localhost:" + randomPort + "/users/login";

    }

    @DisplayName("회원정보 수정 테스트")
    @Test
    public void updateUsersTest() throws Exception {
        //given : 유저 수정 및 저장
        Long id = 3L;
        String password ="@aaaa12345678";
        UpdateUsersDto updateUsersDto = UpdateUsersDto.builder()
                .userName("elice test")
                .userId("elice")
                .email("test@example.com")
                .password(password)
                .phoneNumber("01012341234")
                .userRole(UsersRole.USER)
                .build();
        String encodedPassword = passwordEncoder.encode(updateUsersDto.getPassword());

        //when & then :
        mvc.perform(MockMvcRequestBuilders.post("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateUsersDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value("elice test"))
                .andExpect(jsonPath("$.userId").value("elice"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                //.andExpect(jsonPath("$.password").value(encodedPassword))
                .andExpect(jsonPath("$.phoneNumber").value("01012341234"))
                .andExpect(status().isOk());
        //원본 비밀번호와 암호화된 비밀번호가 일치하는지 확인
        assertThat(passwordEncoder.matches(password, encodedPassword)).isTrue();
    }
    // TODO: 회원정보 삭제 테스트 완성
    @DisplayName("회원정보 삭제 테스트")
    @Test
    public void deleteUsersTest() throws Exception {
        //given : 유저 id
        long id  = 2L;
        SignUpDto signUpDto = SignUpDto.builder()
                .userName("elice delete track")
                .userId("eliceDelete")
                .email("eliceDelete@example.com")
                .password("@abcd12345678")
                .phoneNumber("01043211234")
                .userRole(UsersRole.USER)
                .build();
        usersController.signUp(signUpDto);

        //when : 레퍼지터리 회원 정보 삭제
        usersRepository.deleteById(id);

        //then : 존재하는지 확인
       assertThat(usersRepository.existsById(id)).isEqualTo(false);
    }
}
