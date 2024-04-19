package com.DogFoot.adpotAnimal.order.controller;

import com.DogFoot.adpotAnimal.order.dto.OrderItemRequest;
import com.DogFoot.adpotAnimal.order.dto.OrderItemResponse;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.service.OrderItemService;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderItem")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Long> createOrderItem(OrderItemRequest request) {
        Product product = productService.findProductById(request.getProductId());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        OrderItem createdOrderItem = orderItemService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItem.getId());
    }

    @PostMapping("/lists")
    public ResponseEntity<List<Long>> createOrderItems(@RequestBody List<OrderItemRequest> requests) {
        List<Long> orderItemIds = new ArrayList<>();
        for (OrderItemRequest request : requests) {
            Product product = productService.findProductById(request.getProductId());
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            OrderItem createdOrderItem = orderItemService.create(request);
            orderItemIds.add(createdOrderItem.getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemIds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderDetail(@PathVariable(value = "id") long id) {
        OrderItem orderItem = orderItemService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new OrderItemResponse(orderItem));
    }

    @PostMapping("/api")
    public ResponseEntity<OrderItem> addOrderItem(@RequestBody OrderItemRequest orderItemRequest) {
        OrderItem addedOrderItem = orderItemService.create(orderItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedOrderItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable(value = "id") long id) {
        orderItemService.deleteById(id);

        return ResponseEntity.ok().build();
    }


}
