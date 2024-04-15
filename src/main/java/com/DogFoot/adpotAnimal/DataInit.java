package com.DogFoot.adpotAnimal;

import com.DogFoot.adpotAnimal.order.dto.OrderRequest;
import com.DogFoot.adpotAnimal.order.entity.Address;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.service.DeliveryService;
import com.DogFoot.adpotAnimal.order.service.OrderItemService;
import com.DogFoot.adpotAnimal.order.service.OrderService;
import com.DogFoot.adpotAnimal.products.dto.ProductDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UsersService usersService;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;

    @PostConstruct
    public void init() {
        //가상 유저 데이터

        for (int i = 1; i <= 14; i++) {
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

        ProductDto productDto = new ProductDto(10000, "강아지 단추", 10, 0, null);
        productService.createProduct(productDto);

        productDto = new ProductDto(20000, "강아지 스티커", 10, 0, null);
        productService.createProduct(productDto);

        productDto = new ProductDto(30000, "고양이 단추", 10, 0, null);
        productService.createProduct(productDto);

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
    }
}
