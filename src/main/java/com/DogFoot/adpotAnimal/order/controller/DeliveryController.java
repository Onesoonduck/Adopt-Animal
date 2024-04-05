package com.DogFoot.adpotAnimal.order.controller;


import com.DogFoot.adpotAnimal.order.dto.DeliveryAddressRequest;
import com.DogFoot.adpotAnimal.order.dto.DeliveryAddressResponse;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("")
    public ResponseEntity<Long> addDelivery (@RequestBody DeliveryAddressRequest request) {
        Delivery delivery = deliveryService.create(request.getAddress(), request.getDeliveryName(), request.getDeliveryPhoneNumber());

        return ResponseEntity.status(HttpStatus.CREATED).body(delivery.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryAddressResponse> getDelivery (@PathVariable(value = "id") Long id) {
        Delivery delivery = deliveryService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new DeliveryAddressResponse(delivery));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> updateDelivery (@PathVariable(value = "id") Long id, @RequestBody DeliveryAddressRequest request) {
        deliveryService.update(id, request.getAddress(), request.getDeliveryName(), request.getDeliveryPhoneNumber());

        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery (@PathVariable(value = "id") Long id) {
        deliveryService.deleteById(id);

        return ResponseEntity.ok().build();
    }


}
