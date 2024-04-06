package com.DogFoot.adpotAnimal.order.controller;

import com.DogFoot.adpotAnimal.order.dto.OrderItemRequest;
import com.DogFoot.adpotAnimal.order.dto.OrderItemResponse;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/orderItem")
    public ResponseEntity<Long> createOrderItem (@RequestBody OrderItemRequest request) {
//        product product = productService.findById(request.getProductId());
//        TODO : Product와 연결 후 작업
        OrderItem createdOrderItems = orderItemService.create(product, request.getCount());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItems.getId());
    }

    @PostMapping("/orderItems")
    public ResponseEntity<List<Long>> createOrderItem (@RequestBody List<OrderItemRequest> requests) {
        List<Long> orderItemId = new ArrayList<>();

        for (OrderItemRequest request : requests) {
//        product product = productService.findById(request.getProductId());
//        TODO : Product와 연결 후 작업
            OrderItem createdOrderItems = orderItemService.create(product, request.getCount());
            orderItemId.add(createdOrderItems.getId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemId);
    }

    @GetMapping("/orderItem/{id}")
    public ResponseEntity<OrderItemResponse> getOrderDetail(@PathVariable(value = "id") long id) {
        OrderItem orderItem = orderItemService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new OrderItemResponse(orderItem));
    }

    @DeleteMapping("/orderItem/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable(value = "id") long id) {
        orderItemService.deleteById(id);

        return ResponseEntity.ok().build();
    }


}
