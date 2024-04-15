package com.DogFoot.adpotAnimal.order.controller;

import com.DogFoot.adpotAnimal.order.dto.OrderResponse;
import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.order.entity.OrderStatus;
import com.DogFoot.adpotAnimal.order.service.DeliveryService;
import com.DogFoot.adpotAnimal.order.service.OrderItemService;
import com.DogFoot.adpotAnimal.order.service.OrderService;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order/admin")
public class OrderAdminController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final UsersService usersService;


    // 회원 주문 정보 목록 조회
    @GetMapping("")
    public ResponseEntity<List<OrderResponse>> findOrders(@RequestParam Long usersId) {
        List<OrderResponse> orderResponses = orderService.findAllByUsersId(usersId)
            .stream()
            .map(OrderResponse::new)
            .toList();

        return ResponseEntity.ok().body(orderResponses);
    }

    // 회원의 주문 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable(value = "id") Long id) {
        Order order = orderService.findById(id);

        return ResponseEntity.ok().body(new OrderResponse(order));
    }

    // 회원 주문 배송 상태 수정
    // 주문 상태 변경 api를 하나의 api에서 쿼리파라미터나 바디 사용해서 수정해보기
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable(value = "id") Long id,
        @RequestParam("status") OrderStatus status) {

        if (status == OrderStatus.DELIVERY) {
            orderService.delivery(id);
        } else if (status == OrderStatus.COMPLETE) {
            orderService.complete(id);
        } else if (status == OrderStatus.CANCEL) {
            orderService.cancel(id);
        } else if (status == OrderStatus.REFUND) {
            orderService.refund(id);
        } else {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }

        return ResponseEntity.ok().build();
    }

    // 회원 주문 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsersOrder(@PathVariable(value = "id") Long id) {
        try {
            orderService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 총 주문 수 조회
    @GetMapping("/api/orderCount")
    public ResponseEntity<Long> getUsersCount() {
        Long orderCount = orderService.getOrderCount();
        return ResponseEntity.ok(orderCount);
    }

}
