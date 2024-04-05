package com.DogFoot.adpotAnimal.order.dto;

import com.DogFoot.adpotAnimal.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private final Long id;
    private final List<OrderItemResponse> orderItems;
    private final DeliveryAddressResponse deliveryAddress;
    private final LocalDateTime orderDate;
    private final String orderStatus;

    public OrderResponse (Order order) {
        this.id = order.getId();
        this.orderItems = order.getOrderItems()
            .stream()
            .map(OrderItemResponse::new)
            .toList();
        this.deliveryAddress = new DeliveryAddressResponse(order.getDelivery());
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus().name();
    }
}

