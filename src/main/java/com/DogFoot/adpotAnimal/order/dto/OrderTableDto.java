package com.DogFoot.adpotAnimal.order.dto;

import com.DogFoot.adpotAnimal.order.entity.OrderStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderTableDto {
    private Long id;
    private LocalDateTime orderDate;
    private String firstOrderItem;
    private int orderCount;
    private Long totalPrice;
    private String orderUserId;
    private OrderStatus orderStatus;

    @Builder
    public OrderTableDto(Long id, LocalDateTime orderDate, String firstOrderItem, int orderCount, Long totalPrice, String orderUserId, OrderStatus orderStatus) {
        this.id = id;
        this.orderDate = orderDate;
        this.firstOrderItem = firstOrderItem;
        this.orderCount = orderCount;
        this.totalPrice = totalPrice;
        this.orderUserId = orderUserId;
        this.orderStatus = orderStatus;
    }
}
