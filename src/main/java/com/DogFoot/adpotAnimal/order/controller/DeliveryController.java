package com.DogFoot.adpotAnimal.order.controller;


import com.DogFoot.adpotAnimal.order.dto.DeliveryAddressRequest;
import com.DogFoot.adpotAnimal.order.dto.DeliveryAddressResponse;
import com.DogFoot.adpotAnimal.order.entity.Address;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 배송 정보 생성
    @PostMapping("/api")
    public ResponseEntity<?> addDelivery(@RequestBody @Validated DeliveryAddressRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시 사용자 친화적인 오류 메시지 생성
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // 주소와 상세 주소를 합쳐서 Address 객체 생성
        Address address = Address.builder()
            .city(request.getAddress().getCity())
            .street(request.getAddress().getStreet())
            .zipcode(request.getAddress().getZipcode())
            .build();

        // DeliveryService의 create 메서드에 Address 객체 전달
        Delivery delivery = deliveryService.create(address, request.getReceiverName(), request.getReceiverPhoneNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(delivery.getId());
    }


    // 배송 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryAddressResponse> getDelivery(@PathVariable(value = "id") Long id) {
        Delivery delivery = deliveryService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new DeliveryAddressResponse(delivery));
    }

    // 배송 정보 수정
    @PostMapping("/{id}")
    public ResponseEntity<Long> updateDelivery(@PathVariable(value = "id") Long id, @RequestBody DeliveryAddressRequest request) {
        deliveryService.update(id, request.getAddress(), request.getReceiverName(), request.getReceiverPhoneNumber());
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    // 배송 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable(value = "id") Long id) {

        deliveryService.deleteById(id);

        return ResponseEntity.ok().build();
    }


}
