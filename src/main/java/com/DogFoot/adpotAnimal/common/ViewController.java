package com.DogFoot.adpotAnimal.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {
    @GetMapping("/admin/admin")
    public String adminPage() {
        return "admin/admin";
    }
}