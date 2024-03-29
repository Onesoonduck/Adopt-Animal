package com.DogFoot.adpotAnimal.categories.controller;

import com.DogFoot.adpotAnimal.categories.entity.Categories;
import com.DogFoot.adpotAnimal.categories.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {
    private CategoriesService categoriesService;

    @GetMapping
    public List<Categories> findAll() {
        return categoriesService.findAll();
    }

    @GetMapping("/{id}")
    public Categories findById(@PathVariable Long id) {
        return CategoriesService.findById(id);
    }

    @PostMapping
    public Categories save(@RequestBody Categories categories) {
        return CategoriesService.save(categories);
    }

}
