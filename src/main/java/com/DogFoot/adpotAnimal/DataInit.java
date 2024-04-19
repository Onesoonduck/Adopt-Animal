package com.DogFoot.adpotAnimal;

import com.DogFoot.adpotAnimal.categories.dto.CategoryDto;
import com.DogFoot.adpotAnimal.categories.entity.Category;
import com.DogFoot.adpotAnimal.categories.service.CategoryService;
import com.DogFoot.adpotAnimal.order.entity.Address;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.service.DeliveryService;
import com.DogFoot.adpotAnimal.order.service.OrderItemService;
import com.DogFoot.adpotAnimal.order.service.OrderService;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.service.ProductService;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("local")
public class DataInit {

    private final UsersService usersService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 85; i++) {
            String userId = "test" + i;
            String userName = "test name " + i;
            String password = "Testuser" + i + "!";
            String email = "testemail" + i + "@example.com";
            String phoneNumber = "010123456" + (i < 10 ? "0" + i : i);

            SignUpDto user = SignUpDto.builder()
                .userId(userId.toString())
                .userName(userName)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .userRole(UsersRole.USER)
                .build();

            usersService.signUp(user);
        }

        SignUpDto admin = SignUpDto.builder()
            .userId("admin")
            .userName("elice admin")
            .password("Admin1234!")
            .email("eliceadmin1234@example.com")
            .phoneNumber("01023456789")
            .userRole(UsersRole.ADMIN)
            .build();
        usersService.signUp(admin);

        // 카테고리 추가
        CategoryDto categoryDto = CategoryDto.builder()
            .categoryName("장난감")
            .categoryImg("/images/2024-04-16/jangnangam.jpg")
            .build();
        categoryService.createCategory(categoryDto);
        categoryDto = CategoryDto.builder()
            .categoryName("폰 액세서리")
            .categoryImg("/images/2024-04-16/smart.jpg")
            .build();
        categoryService.createCategory(categoryDto);

        // 상품 추가
        Product product = new Product(9800, "누렁이 스마트톡", 15, 0,
            "/images/2024-04-16/1.jpg",
            categoryService.findByCategoryId(2L));
        productService.createProduct(product.toDto());

        product = new Product(8800, "고양이 스마트톡", 7, 0,
            "/images/2024-04-16/2.png",
            categoryService.findByCategoryId(2L));
        productService.createProduct(product.toDto());

        product = new Product(12000, "과일 장난감", 17, 0,
            "/images/2024-04-16/3.jpg",
            categoryService.findByCategoryId(1L));
        productService.createProduct(product.toDto());

        product = new Product(4000, "강아지 인형", 40, 0,
            "/images/2024-04-16/4.jpeg",
            categoryService.findByCategoryId(1L));
        productService.createProduct(product.toDto());

        /*
        for (int i = 1; i <= 78; i++) {
            product = new Product(7800, "누렁이 스마트톡 " + i, 10, 0,
                "/images/2024-04-16/1.jpg",
                categoryService.findByCategoryId(2L));
            productService.createProduct(product.toDto());
        }
        */
        // 주문 추가
        Users users = usersService.findUserById(1L);

        List<OrderItem> list = new ArrayList<>();
        list.add(OrderItem.createOrderItem(productService.findProductById(1L), 98000, 10));
        list.add(OrderItem.createOrderItem(productService.findProductById(3L), 24000, 2));
        List<OrderItem> orderItems = list;

        Address address = new Address("서울", "123", "58067");
        Delivery delivery = Delivery.createDelivery(address, "Tester", "010-0000-0000");
        orderService.create(users, delivery, orderItems);

        //
        List<OrderItem> list2 = new ArrayList<>();
        list2.add(OrderItem.createOrderItem(productService.findProductById(1L), 9800, 10));
        orderItems = list2;

        Delivery delivery2 = Delivery.createDelivery(address, "Tester2", "010-1111-0000");
        orderService.create(usersService.findUserById(2L), delivery2, orderItems);
    }
}
