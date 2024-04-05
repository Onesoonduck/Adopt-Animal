package com.DogFoot.adpotAnimal.order.dto;

import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {

    private final Long productId;
    private final int count;
    private final int orderPrice;
    private final int totalPrice;

    public OrderItemResponse (OrderItem orderItem) {
//        this.productId = orderItem.getProduct().getId();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.totalPrice = orderItem.getTotalPrice();
    }
}
