package com.DogFoot.adpotAnimal.order.dto;

import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class OrderItemResponse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private final int count;
    private final int orderPrice;
    private final int totalPrice;

    public OrderItemResponse (OrderItem orderItem) {
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.totalPrice = orderItem.getTotalPrice();
    }
}
