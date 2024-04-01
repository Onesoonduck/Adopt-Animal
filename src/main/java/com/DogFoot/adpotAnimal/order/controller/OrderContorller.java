package com.DogFoot.adpotAnimal.order.controller;

import com.DogFoot.adpotAnimal.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequiredArgsConstructor
public class OrderContorller {

    private final OrderService orderService;
    // TODO: 유저, 상품 Service 추가


    // TODO: 유저, 상품 객체/메서드 추가
    @GetMapping("")
    public String createForm() {

    }

    @PostMapping("")
    public String order () {

    }

    @PutMapping("")
    public String orderUpdate () {

    }

    @DeleteMapping("")
    public String orderDelete () {

    }

}
