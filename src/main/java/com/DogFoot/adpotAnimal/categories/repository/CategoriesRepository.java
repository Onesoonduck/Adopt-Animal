package com.DogFoot.adpotAnimal.categories.repository;

import com.DogFoot.adpotAnimal.categories.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Optional<Categories> findById(Long categoryId);
}
