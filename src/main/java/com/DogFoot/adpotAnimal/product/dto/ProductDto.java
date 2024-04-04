package com.DogFoot.adpotAnimal.product.dto;

import com.DogFoot.adpotAnimal.product.entity.ProductEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private Integer category_id;
    private int price;
    private String product_name;
    private int product_stock;
    private int likes;

    public static ProductEntity toEntity(ProductDto productDto) {
        ProductEntity product = new ProductEntity();
        product.setId(productDto.getId());
        product.setCategory_id(productDto.getCategory_id());
        product.setPrice(productDto.getPrice());
        product.setProduct_name(productDto.getProduct_name());
        product.setProduct_stock(productDto.getProduct_stock());
        product.setLikes(productDto.getLikes());
        return product;
    }

    public static ProductDto fromDto(ProductEntity product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setCategory_id(product.getCategory_id());
        productDto.setPrice(product.getPrice());
        productDto.setProduct_name(product.getProduct_name());
        productDto.setProduct_stock(product.getProduct_stock());
        productDto.setLikes(product.getLikes());
        return productDto;
    }
}
