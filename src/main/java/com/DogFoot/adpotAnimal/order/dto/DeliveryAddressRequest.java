package com.DogFoot.adpotAnimal.order.dto;

import com.DogFoot.adpotAnimal.order.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressRequest {

    private Address address;
    private String deliveryName;
    private String deliveryPhoneNumber;
}
