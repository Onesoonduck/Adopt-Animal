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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
//@Profile("local")
public class DataInit {

    private final UsersService usersService;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final CategoryService categoryService;

    @PostConstruct
    public void init() {
        //가상 유저 데이터

        for (int i = 1; i <= 13; i++) {
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
        usersService.signUp(admin);

        // 카테고리 추가
        CategoryDto categoryDto = CategoryDto.builder()
            .categoryName("문구")
            .categoryImg(null)
            .build();
        Category category = categoryService.createCategory(categoryDto);
        categoryDto = CategoryDto.builder()
            .categoryName("폰")
            .categoryImg(null)
            .build();
        categoryService.createCategory(categoryDto);
        categoryDto = CategoryDto.builder()
            .categoryName("이어폰")
            .categoryImg(null)
            .build();
        categoryService.createCategory(categoryDto);

        // 상품 추가
        Product product = new Product(10000, "강아지 단추", 10, 0,
            "/images/2024-04-16/456fa6f6-6098-44a4-92a4-a9923a11e20a.jpg",
            categoryService.findByCategoryId(1L));
        productService.createProduct(product.toDto());

        product = new Product(20000, "강아지 스티커", 10, 0,
            "/images/2024-04-16/d6b0222b-c664-4bdd-8eb7-aba64cf75bdb.jpg",
            categoryService.findByCategoryId(1L));
        productService.createProduct(product.toDto());

        product = new Product(30000, "고양이 단추", 10, 0,
            "/images/2024-04-16/ed596cc4-a361-4a94-9d04-269135471928.png",
            categoryService.findByCategoryId(2L));
        productService.createProduct(product.toDto());

        product = new Product(2500, "강아지 무지 노트", 40, 0,
            "/images/2024-04-18/a70314d8-edf1-4fe4-af63-55438d1eac86.jpg",
            categoryService.findByCategoryId(3L));
        productService.createProduct(product.toDto());

        for (int i = 1; i <= 12; i++) {
            product = new Product(15000, "고양이 단추" + i, 40, 0,
                "/images/2024-04-16/456fa6f6-6098-44a4-92a4-a9923a11e20a.jpg",
                categoryService.findByCategoryId(1L));
            productService.createProduct(product.toDto());
        }

        for (int i = 1; i <= 12; i++) {
            product = new Product(2500, "강아지 무지 노트" + i, 40, 0,
                "/images/2024-04-18/a70314d8-edf1-4fe4-af63-55438d1eac86.jpg",
                categoryService.findByCategoryId(3L));
            productService.createProduct(product.toDto());
        }

        // 주문 추가
        Users users = usersService.findUserById(1L);

        List<OrderItem> list = new ArrayList<>();
        list.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        list.add(OrderItem.createOrderItem(productService.findProductById(2L), 50000, 10));
        list.add(OrderItem.createOrderItem(productService.findProductById(3L), 10000, 10));
        List<OrderItem> orderItems = list;

        Address address = new Address("서울", "123", "58067");
        Delivery delivery = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery, orderItems);

        //
        List<OrderItem> list2 = new ArrayList<>();
        list2.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list2;

        Delivery delivery2 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(usersService.findUserById(2L), delivery2, orderItems);

        List<OrderItem> list3 = new ArrayList<>();
        list3.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list3;

        Delivery delivery3 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery3, orderItems);

        List<OrderItem> list4 = new ArrayList<>();
        list4.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list4;
        Delivery delivery4 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery4, orderItems);

        List<OrderItem> list5 = new ArrayList<>();
        list5.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list5;
        Delivery delivery5 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery5, orderItems);

        List<OrderItem> list6 = new ArrayList<>();
        list6.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list6;
        Delivery delivery6 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery6, orderItems);

        List<OrderItem> list7 = new ArrayList<>();
        list7.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list7;
        Delivery delivery7 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery7, orderItems);

        List<OrderItem> list8 = new ArrayList<>();
        list8.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list8;
        Delivery delivery8 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery8, orderItems);

        List<OrderItem> list9 = new ArrayList<>();
        list9.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list9;
        Delivery delivery9 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery9, orderItems);

        List<OrderItem> list10 = new ArrayList<>();
        list10.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list10;
        Delivery delivery10 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery10, orderItems);

        List<OrderItem> list11 = new ArrayList<>();
        list11.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list11;
        Delivery delivery11 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery11, orderItems);

        List<OrderItem> list12 = new ArrayList<>();
        list12.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        orderItems = list12;
        Delivery delivery12 = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
        orderService.create(users, delivery12, orderItems);
        orderService.delivery(12L);


    }
}
