package com.DogFoot.adpotAnimal.order;
import com.DogFoot.adpotAnimal.order.entity.*;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.service.ProductService;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderTest {

    private Users users;
    private List<OrderItem> orderItems;
    private Delivery delivery;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ProductService productService;

    @BeforeEach
    public void setup() {

        List<OrderItem> list = new ArrayList<>();
        list.add(OrderItem.createOrderItem(productService.findProductById(1L), 20000, 10));
        list.add(OrderItem.createOrderItem(productService.findProductById(2L), 50000, 10));
        list.add(OrderItem.createOrderItem(productService.findProductById(3L), 10000, 10));
        orderItems = list;


        Address address = new Address("서울", "123", "58067");
        delivery = Delivery.createDelivery(address, "받는사람", "010-0000-0000");
    }

    @DisplayName("주문 객체 생성 테스트")
    @Test
    public void addOrderTest() {

        // when
        Order order = Order.createOrder(users, delivery, orderItems);

        // then
        assertEquals(order.getUsers(), users);
        assertEquals(order.getDelivery(), delivery);
        assertEquals(order.getOrderItems(), orderItems);
        assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
    }

    // 주문 상태 취소 관련 테스트
    @DisplayName("주문 상태 취소 변경 테스트")
    @Test
    public void orderStatusCancelTest(){

        // given
        Order order = Order.createOrder(users, delivery, orderItems);

        // when
        order.cancel();

        // then
        assertEquals(order.getOrderStatus(), OrderStatus.CANCEL);
    }

    @DisplayName("배송중인 주문을 주문 취소로 변경하는 실패 테스트")
    @Test
    public void orderStatusCancelFailTest1(){

        Order order = Order.createOrder(users, delivery, orderItems);
        order.delivery();

        assertThatThrownBy(() -> order.cancel())
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("배송 완료 주문을 취소로 변경하는 실패 테스트")
    @Test
    public void orderStatusCancelFailTest2(){

        Order order = Order.createOrder(users, delivery, orderItems);
        order.complete();

        assertThatThrownBy(() -> order.cancel())
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("이미 취소된 주문을 주문 취소로 변경하는 실패 테스트")
    @Test
    public void orderStatusCancelFailTest3(){

        Order order = Order.createOrder(users, delivery, orderItems);
        order.cancel();

        assertThatThrownBy(() -> order.cancel())
            .isInstanceOf(IllegalStateException.class);
    }

    // 주문 상태 배송중 관련 테스트
    @DisplayName("주문 상태 배송중으로 변경 테스트")
    @Test
    public void orderStatusDeliveryTest(){

        // given
        Order order = Order.createOrder(users, delivery, orderItems);

        // when
        order.delivery();

        // then
        assertEquals(order.getOrderStatus(), OrderStatus.DELIVERY);
    }

    @DisplayName("이미 배송중인 주문을 배송중으로 변경하는 실패 태스트")
    @Test
    public void orderStatusDeliveryFailTest1(){

        Order order = Order.createOrder(users, delivery, orderItems);
        order.delivery();

        assertThatThrownBy(() -> order.delivery())
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("취소된 상품을 배송중으로 변경하는 실패 태스트")
    @Test
    public void orderStatusDeliveryFailTest2(){

        Order order = Order.createOrder(users, delivery, orderItems);
        order.cancel();

        assertThatThrownBy(() -> order.delivery())
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("배송 완료된 상품을 배송중으로 변경하는 실패 태스트")
    @Test
    public void orderStatusDeliveryFailTest3(){

        Order order = Order.createOrder(users, delivery, orderItems);
        order.cancel();

        assertThatThrownBy(() -> order.delivery())
            .isInstanceOf(IllegalStateException.class);
    }

    // 주문 상태 배송 완료 관련 테스트
    @DisplayName("주문 상태 배송 완료 변경 테스트")
    @Test
    public void orderStatusCompleteTest(){

        // given
        Order order = Order.createOrder(users, delivery, orderItems);
        order.delivery();

        // when
        order.complete();

        // then
        assertEquals(order.getOrderStatus(), OrderStatus.COMPLETE);
    }

    @DisplayName("취소된 주문을 배송 완료로 변경하는 실패 테스트")
    @Test
    public void orderStatusCompleteFailTest1(){

        Order order = Order.createOrder(users, delivery, orderItems);
        order.cancel();

        assertThatThrownBy(() -> order.complete())
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("배송 완료된 주문을 배송 완료로 변경하는 실패 테스트")
    @Test
    public void orderStatusCompleteFailTest2(){

        Order order = Order.createOrder(users, delivery, orderItems);
        order.complete();

        assertThatThrownBy(() -> order.complete())
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("getTotalPrice(): 총 금액 테스트")
    @Test
    public void totalPriceTest() {

        Order order = Order.createOrder(users, delivery, orderItems);
        int totalPrice = orderItems.stream()
            .mapToInt(OrderItem::getTotalPrice)
            .sum();

        assertEquals(order.getTotalPrice(), totalPrice);
    }
}
