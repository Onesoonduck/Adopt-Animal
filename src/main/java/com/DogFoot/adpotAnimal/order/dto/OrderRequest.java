package com.DogFoot.adpotAnimal.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderRequest {

    private Long usersId;
    private Long deliveryId;
    private List<Long> orderItemId;
}
