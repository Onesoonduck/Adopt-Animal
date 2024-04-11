package com.DogFoot.adpotAnimal.categories.service;

import com.DogFoot.adpotAnimal.categories.entity.Categories;
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

    public List<Categories> findAll() {
        return categoriesRepository.findAll();
    }

    public Categories findById(Long categoryId) {
        return categoriesRepository.findById(categoryId).orElseThrow(() ->
            new IllegalArgumentException("잘못된 접근입니다."));
    }

    public Categories createCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }

    public Categories updateCategories(Categories categories) {
        Categories updateCategories = categoriesRepository.findById(categories.getCategoryId())
            .orElseThrow(() -> new RuntimeException());

        return categoriesRepository.save(updateCategories);
    }

    public void deleteCategories(long categoryId) {
        Categories categories = categoriesRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException());

        categoriesRepository.delete(categories);
    }
}
