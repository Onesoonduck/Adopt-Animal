package com.DogFoot.adpotAnimal.order.dto;

import com.DogFoot.adpotAnimal.order.entity.Address;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import lombok.Getter;

@Getter
public class DeliveryAddressResponse {

    private final Address address;
    private final String receiverName;
    private final String receiverPhoneNumber;

    public DeliveryAddressResponse (Delivery delivery) {
        this.address = delivery.getAddress();
        this.receiverName = delivery.getReceiverName();
        this.receiverPhoneNumber = delivery.getReceiverPhoneNumber();
    }
}
