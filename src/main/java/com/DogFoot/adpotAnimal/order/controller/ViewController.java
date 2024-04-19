package com.DogFoot.adpotAnimal.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    // 주문 페이지
    @PostMapping("/orderPage")
    public String orderPage() {
        return "orderPage";
    }

    @PostMapping("/orderComplete")
    public String orderComplete() {
        return "orderComplete";
    }
}
