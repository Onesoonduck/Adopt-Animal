package com.DogFoot.adpotAnimal.order.service;

import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.repository.OrderItemRepository;
import com.DogFoot.adpotAnimal.products.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem create (Product product, int orderPrice, int count) {
        return orderItemRepository.save(OrderItem.createOrderItem(product, orderPrice, count));
    }

    public OrderItem findById (Long id) {
        return orderItemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Found OrderItem Id: " + id));
    }

    public void deleteById (Long id) {
        findById(id);
        orderItemRepository.deleteById(id);
    }

}
