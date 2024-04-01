package com.DogFoot.adpotAnimal.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private Integer category_id;
    private int price;
    private String product_name;
    private int product_stock;
    private int like;

    public ProductDto(Long id, Integer category_id, int price, String product_name, int product_stock, int like) {
        this.id = id;
        this.category_id = category_id;
        this.price = price;
        this.product_name = product_name;
        this.product_stock = product_stock;
        this.like = like;

    }

}
