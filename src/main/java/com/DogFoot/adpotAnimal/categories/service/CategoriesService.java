package com.DogFoot.adpotAnimal.categories.service;

import com.DogFoot.adpotAnimal.categories.entity.Category;
import com.DogFoot.adpotAnimal.categories.repository.CategoriesRepository;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Category> findAll() {
        return categoriesRepository.findAll();
    }

    public Category findById(Long categoryId) {
        return categoriesRepository.findById(categoryId).orElseThrow(() ->
            new IllegalArgumentException("잘못된 접근입니다."));
    }

    public Category createCategories(Category categories) {
        return categoriesRepository.save(categories);
    }

    public Category updateCategories(Category categories) {
        Category updateCategories = categoriesRepository.findById(categories.getCategoryId())
            .orElseThrow(() -> new RuntimeException());

        return categoriesRepository.save(updateCategories);
    }

    public void deleteCategories(long categoryId) {
        Category categories = categoriesRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException());

        categoriesRepository.delete(categories);
    }
}
