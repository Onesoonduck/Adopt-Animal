package com.DogFoot.adpotAnimal.categories.controller;

import com.DogFoot.adpotAnimal.categories.dto.CategoryDto;
import com.DogFoot.adpotAnimal.categories.entity.Category;
import com.DogFoot.adpotAnimal.categories.service.CategoryService;
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
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 카테고리 전체 조회
    @GetMapping()
    public ResponseEntity<List<Category>> findAllCategory() {
        List<Category> categories = categoryService.findAllCategory();

        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // 카테고리 ID별 조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> findByCategoryId(@PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.findByCategoryId(categoryId);

        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    // 카테고리 추가
    @PostMapping("/add")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) {
        Category newCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable long categoryId, @RequestBody CategoryDto categoryDto) {
        Category updatedCategory = categoryService.updateCategory(categoryId, categoryDto);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
