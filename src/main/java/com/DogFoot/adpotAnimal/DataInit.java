package com.DogFoot.adpotAnimal;

import com.DogFoot.adpotAnimal.products.dto.ProductDto;
import com.DogFoot.adpotAnimal.products.service.ProductService;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UsersService usersService;
    private final ProductService productService;

    @PostConstruct
    public void init() {
        //가상 유저 데이터

        for (int i = 1; i <= 64; i++) {
            String userId = "test" + i;
            String userName = "test" + i;
            String password = "Testuser" + i + "!";
            String email = "testuser" + i + "@example.com";
            String phoneNumber = "010123456" + (i < 10 ? "0" + i : i);

            SignUpDto user = SignUpDto.builder()
                .userId(userId)
                .userName(userName)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .userRole(UsersRole.USER)
                .build();

            usersService.signUp(user);
        }

        SignUpDto admin = SignUpDto.builder()
            .userId("eliceadmin")
            .userName("eliceadmin")
            .password("Eliceadmin1234!")
            .email("eliceadmin1234@example.com")
            .phoneNumber("01023456789")
            .userRole(UsersRole.ADMIN)
            .build();


        ProductDto productDto = new ProductDto(10000, "강아지 단추", 10, 0, null);
        productService.createProduct(productDto);

        productDto = new ProductDto(20000, "강아지 스티커", 10, 0, null);
        productService.createProduct(productDto);

        productDto = new ProductDto(30000, "고양이 단추", 10, 0, null);
        productService.createProduct(productDto);


        usersService.signUp(admin);
    }
}
