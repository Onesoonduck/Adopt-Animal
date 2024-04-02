package com.DogFoot.adpotAnimal.dto;

import com.DogFoot.adpotAnimal.entity.Product;
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
    private int like;

    public static Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setCategory_id(productDto.getCategory_id());
        product.setPrice(productDto.getPrice());
        product.setProduct_name(productDto.getProduct_name());
        product.setProduct_stock(productDto.getProduct_stock());
        product.setLike(productDto.getLike());
        return product;
    }

    public static ProductDto fromDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setCategory_id(product.getCategory_id());
        productDto.setPrice(product.getPrice());
        productDto.setProduct_name(product.getProduct_name());
        productDto.setProduct_stock(product.getProduct_stock());
        productDto.setLike(product.getLike());
        return productDto;
    }
}
