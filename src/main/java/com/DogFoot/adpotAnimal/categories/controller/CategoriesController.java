package com.DogFoot.adpotAnimal.categories.controller;

import com.DogFoot.adpotAnimal.categories.entity.Categories;
import com.DogFoot.adpotAnimal.categories.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {
    private CategoriesService categoriesService;

    @Autowired
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    // 카테고리 전체 조회
    @GetMapping
    public ResponseEntity<List<Categories>> findAll() {
        List<Categories> categories = categoriesService.findAll();

        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // 카테고리 ID별 조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<Categories> findById(@PathVariable("categoryId") Long categoryId) {
        Categories categories = categoriesService.findById(categoryId);

        if (categories == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // 카테고리 추가
    @PostMapping("/add")
    public ResponseEntity<Categories> createCategories(@RequestBody Categories categories) {
        Categories newCategory = categoriesService.createCategories(categories);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<Categories> updateCategories(@PathVariable long categoryId, @RequestBody Categories categories) {
        categories.setCategoryId(categoryId);
        Categories updatedCategory = categoriesService.updateCategories(categories);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Categories> deleteCategories(@PathVariable long categoryId) {
        categoriesService.deleteCategories(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
