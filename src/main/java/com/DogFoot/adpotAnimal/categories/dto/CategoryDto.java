package com.DogFoot.adpotAnimal.categories.dto;

import com.DogFoot.adpotAnimal.categories.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String categoryName;
    private String categoryImg;

    @Builder
    public CategoryDto(String categoryName, String categoryImg) {
        this.categoryName = categoryName;
        this.categoryImg = categoryImg;
    }

    public static Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        return category;
    }

    public static CategoryDto fromDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName(category.getCategoryName());
        categoryDto.setId(categoryDto.getId());
        return categoryDto;
    }
}
