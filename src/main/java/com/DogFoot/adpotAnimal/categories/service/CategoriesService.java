package com.DogFoot.adpotAnimal.categories.service;

import com.DogFoot.adpotAnimal.categories.entity.Categories;
import com.DogFoot.adpotAnimal.categories.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public List<Categories> findAll() {
        return categoriesRepository.findAll();
    }

    public Categories findById(Long id) {
        return CategoriesRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("잘못된 접근입니다."));
    }

    public Categories save(Categories categories) {
        return CategoriesRepository.save(categories);
    }

}
