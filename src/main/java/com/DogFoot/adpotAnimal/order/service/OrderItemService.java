package com.DogFoot.adpotAnimal.order.service;

import com.DogFoot.adpotAnimal.order.dto.OrderItemRequest;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.repository.OrderItemRepository;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    // 주문 상품 추가
    @Transactional
    public OrderItem create(OrderItemRequest orderItemRequest) {
        Product product = productRepository.findById(orderItemRequest.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + orderItemRequest.getProductId()));
        OrderItem orderItem = orderItemRepository.save(OrderItem.createOrderItem(product, orderItemRequest.getPrice(), orderItemRequest.getCount()));
        return orderItem;
    }

    // 주문 상품 조회
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Found OrderItem Id: " + id));
    }

    // 주문 상품 취소
    public void deleteById(Long id) {
        findById(id);
        orderItemRepository.deleteById(id);
    }

}
