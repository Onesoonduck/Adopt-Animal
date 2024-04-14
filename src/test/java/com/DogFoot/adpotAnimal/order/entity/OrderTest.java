package com.DogFoot.adpotAnimal.order.entity;

import com.DogFoot.adpotAnimal.order.service.OrderService;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.users.entity.Users;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private OrderService orderService;


    // 주문 생성 로직 테스트
    @Test
    void testCreateOrder() {

        Users mockUser = new Users("Test", "test", "1234", "test@test.com", "01012345678", null);

        Delivery mockDelivery = new Delivery();

        List<OrderItem> mockOrderItems = new ArrayList<>();

        Order order = Order.createOrder(mockUser, mockDelivery, mockOrderItems);

        assertNotNull(order);
        assertEquals(mockUser, order.getUsers());
        assertEquals(mockDelivery, order.getDelivery());
        assertEquals(mockOrderItems.size(), order.getOrderItems().size());
        assertEquals(OrderStatus.ORDER, order.getOrderStatus());
        assertNotNull(order.getOrderDate());
        assertTrue(order.getOrderDate().isBefore(LocalDateTime.now()));
    }



    // 주문 취소 로직 테스트
    @Test
    void testCancel() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETE);

        order.setOrderStatus(OrderStatus.DELIVERY);
        IllegalStateException deliveryException = assertThrows(IllegalStateException.class, () -> {
            order.cancel();
        });
        assertEquals("배송 중인 상품은 취소가 불가능합니다.", deliveryException.getMessage());

        order.setOrderStatus(OrderStatus.COMPLETE);
        IllegalStateException completeException = assertThrows(IllegalStateException.class, () -> {
            order.cancel();
        });
        assertEquals("이미 배송이 완료된 상품은 취소가 불가능합니다.", completeException.getMessage());

        order.setOrderStatus(OrderStatus.CANCEL);
        IllegalStateException cancelException = assertThrows(IllegalStateException.class, () -> {
            order.cancel();
        });
        assertEquals("이미 취소된 상품입니다.", cancelException.getMessage());

        order.setOrderStatus(OrderStatus.REFUND);
        IllegalStateException refundException = assertThrows(IllegalStateException.class, () -> {
            order.cancel();
        });
        assertEquals("이미 환불된 상품입니다.", refundException.getMessage());

        order.setOrderStatus(OrderStatus.ORDER);
        assertDoesNotThrow(() -> {
            order.cancel();
        });
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
    }



    // 주문 시 재고 감소 로직 테스트
    @Test
    void testCreateOrderItem() {
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProduct_price(10000);
        product.setProduct_stock(5);

        int orderPrice = 10000;
        int count = 3;

        OrderItem orderItem = OrderItem.createOrderItem(product, orderPrice, count);

        assertEquals(product, orderItem.getProduct());
        assertEquals(orderPrice, orderItem.getOrderPrice());
        assertEquals(count, orderItem.getCount());

        assertEquals(2, product.getProduct_stock());
    }



    // 주문 취소시 재고 복구 로직 테스트
    @Test
    void testOrderItemCancel() {

        Product product = new Product();
        product.setProductName("Test Product");
        product.setProduct_price(10000);
        product.setProduct_stock(10);

        int orderPrice = 8000;
        int count = 4;

        OrderItem orderItem = new OrderItem();

        orderItem.setProduct(product);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        orderItem.cancel();

        assertEquals(10 + count, product.getProduct_stock());
    }

}