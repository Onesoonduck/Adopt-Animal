package com.DogFoot.adpotAnimal.order.service;

import com.DogFoot.adpotAnimal.order.entity.Address;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Delivery findById (Long id) {
        return deliveryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Found Delivery id: " + id));
    }

    public Delivery create (Address address, String receiverName, String receiverPhoneNumber) {
        Delivery delivery = new Delivery();

        return deliveryRepository.save(delivery);
    }

    public Delivery update (Long id, Address address, String receiverName, String receiverPhoneNumber) {
        Delivery delivery = findById(id);

        delivery.update(address, receiverName, receiverPhoneNumber);

        return delivery;
    }

    public void deleteById (Long id) {
        findById(id);

        deliveryRepository.deleteById(id);
    }

}
