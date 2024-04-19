package com.DogFoot.adpotAnimal.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class OrderRequest {

    private Long usersId;
    private Long deliveryId;
    private List<Long> orderItemId;

    // 모든 필드를 포함한 생성자
    public OrderRequest(Long usersId, Long deliveryId, List<Long> orderItemId) {
        this.usersId = usersId;
        this.deliveryId = deliveryId;
        this.orderItemId = orderItemId;
    }
}
