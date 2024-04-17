package com.DogFoot.adpotAnimal.categories.entity;

import com.DogFoot.adpotAnimal.categories.dto.CategoryDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotNull
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_img")
    private String categoryImg;

    @Builder
    public Category(String categoryName, String categoryImg) {
        this.categoryName = categoryName;
        this.categoryImg = categoryImg;
    }

    public CategoryDto toCategoryTableDto() {
        CategoryDto categoryDto;
        categoryDto = new CategoryDto();

        categoryDto.setCategoryName(categoryDto.getCategoryName());
        categoryDto.setCategoryImg(categoryDto.getCategoryImg());

        return categoryDto;
    }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public void setCategoryImg(String categoryImg) { this.categoryImg = categoryImg; }
}
