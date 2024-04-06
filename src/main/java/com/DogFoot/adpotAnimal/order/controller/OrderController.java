package com.DogFoot.adpotAnimal.order.controller;

import com.DogFoot.adpotAnimal.order.dto.OrderRequest;
import com.DogFoot.adpotAnimal.order.dto.OrderResponse;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.service.DeliveryService;
import com.DogFoot.adpotAnimal.order.service.OrderItemService;
import com.DogFoot.adpotAnimal.order.service.OrderService;
import com.DogFoot.adpotAnimal.users.entity.CustomUserDetails;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.service.CustomUserDetailsService;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final UsersService usersService;


    // TODO: 유저, 상품 추가
    @PostMapping("/order")
    public ResponseEntity<Long> addOrder (@RequestBody OrderRequest request) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userDetails.getUser();

        Delivery delivery = deliveryService.findById(request.getDeliveryId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (Long orderItemId : request.getOrderItemId()) {
            orderItems.add(orderItemService.findById(orderItemId));
        }

        Order order = orderService.create(users, delivery, orderItems);

        return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> findOrders (@RequestParam Long usersId) {
        List<OrderResponse> orderResponses = orderService.findAllByMemberId(usersId)
            .stream()
            .map(OrderResponse::new)
            .toList();

        return ResponseEntity.ok().body(orderResponses);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> findOrder (@PathVariable(value = "id") Long id) {
        Order order = orderService.findById(id);

        return ResponseEntity.ok().body(new OrderResponse(order));
    }

    // 주문 상태
    @PutMapping("/order/{id}/delivery")
    public ResponseEntity<Void> updateStatusDelivery (@PathVariable(value = "id") Long id) {
        orderService.delivery(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/order/{id}/complete")
    public ResponseEntity<Void> updateStatusComplete (@PathVariable(value = "id") Long id) {
        orderService.complete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/order/{id}/cancel")
    public ResponseEntity<Void> updateStatusCancel (@PathVariable(value = "id") Long id) {
        orderService.cancel(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Void> deleteOrder (@PathVariable(value = "id") Long id) {
        orderService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
