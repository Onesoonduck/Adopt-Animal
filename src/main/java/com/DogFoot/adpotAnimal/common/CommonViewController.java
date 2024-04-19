package com.DogFoot.adpotAnimal.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CommonViewController {
    @GetMapping("/")
    public String mainview() {
        return "redirect:/main.html";
    }
}