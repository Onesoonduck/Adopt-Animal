package com.DogFoot.adpotAnimal.order.controller;

import com.DogFoot.adpotAnimal.order.dto.OrderRequest;
import com.DogFoot.adpotAnimal.order.dto.OrderResponse;
import com.DogFoot.adpotAnimal.order.dto.OrderTableDto;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.service.DeliveryService;
import com.DogFoot.adpotAnimal.order.service.OrderItemService;
import com.DogFoot.adpotAnimal.order.service.OrderService;
import com.DogFoot.adpotAnimal.users.dto.UsersTableDto;
import com.DogFoot.adpotAnimal.users.entity.CustomUserDetails;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final UsersService usersService;

    // 주문 생성
    @PostMapping("")
    public ResponseEntity<Long> addOrder(@RequestBody OrderRequest request) {

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

    // 주문 목록 검색
    @GetMapping("")
    public ResponseEntity<List<OrderResponse>> findOrders(@RequestParam Long usersId) {
        List<OrderResponse> orderResponses = orderService.findAllByUsersId(usersId)
            .stream()
            .map(OrderResponse::new)
            .toList();

        return ResponseEntity.ok().body(orderResponses);
    }

    // 특정 주문 검색
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable(value = "id") Long id) {
        Order order = orderService.findById(id);

        return ResponseEntity.ok().body(new OrderResponse(order));
    }

    // 장바구니, 주문 상품 목록 가져오기


    @GetMapping("/api/orderTable")
    public ResponseEntity<Page<OrderTableDto>> getUserTable(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderTableDto> orderDtoPage = orderService.getOrderTable(pageable);
        return ResponseEntity.ok(orderDtoPage);
    }

    // 주문수 조회
    @GetMapping("/api/orderCount")
    public ResponseEntity<Long> getOrderCount(HttpServletResponse response) {
        Long orderCount = orderService.getOrderCount();
        return ResponseEntity.ok(orderCount);
    }
}
