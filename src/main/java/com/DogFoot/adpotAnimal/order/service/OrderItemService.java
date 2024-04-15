package com.DogFoot.adpotAnimal.order.service;

import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.repository.OrderItemRepository;
import com.DogFoot.adpotAnimal.products.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    // 주문 상품 추가
    @Transactional
    public OrderItem create(Product product, int orderPrice, int count) {
        return orderItemRepository.save(OrderItem.createOrderItem(product, orderPrice, count));
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
