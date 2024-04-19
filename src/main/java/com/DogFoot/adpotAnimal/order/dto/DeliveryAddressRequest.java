package com.DogFoot.adpotAnimal.order.dto;

import com.DogFoot.adpotAnimal.order.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryAddressRequest {

    private Address address;
    private String receiverName;
    private String receiverPhoneNumber;

    @Builder
    public DeliveryAddressRequest(Address address, String receiverName, String receiverPhoneNumber) {
        this.address = address;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
    }
}
