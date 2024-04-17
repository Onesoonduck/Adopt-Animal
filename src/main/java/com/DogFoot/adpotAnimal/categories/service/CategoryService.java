package com.DogFoot.adpotAnimal.categories.service;

import com.DogFoot.adpotAnimal.categories.dto.CategoryDto;
import com.DogFoot.adpotAnimal.categories.entity.Category;
import com.DogFoot.adpotAnimal.categories.repository.CategoryRepository;
import java.io.Console;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    public Category findByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
            new IllegalArgumentException("잘못된 접근입니다."));
    }

    public Category createCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
            .categoryName(categoryDto.getCategoryName())
            .categoryImg(categoryDto.getCategoryImg())
            .build();
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category updateCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        updateCategory.setCategoryName(categoryDto.getCategoryName());
        updateCategory.setCategoryImg(categoryDto.getCategoryImg());

        return categoryRepository.save(updateCategory);
    }

    public void deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        categoryRepository.delete(category);
    }
}
