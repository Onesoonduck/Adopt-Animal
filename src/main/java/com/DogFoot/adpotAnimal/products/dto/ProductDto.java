package com.DogFoot.adpotAnimal.products.dto;

import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.products.entity.Product;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private CartEntity cart;
    private int product_price;
    private String product_name;
    private int product_stock;
    private int product_like;

    public static Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setProduct_price(productDto.getProduct_price());
        product.setProduct_name(productDto.getProduct_name());
        product.setProduct_stock(productDto.getProduct_stock());
        product.setProduct_like(productDto.getProduct_like());
        return product;
    }

    public static ProductDto fromDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setProduct_price(product.getProduct_price());
        productDto.setProduct_name(product.getProduct_name());
        productDto.setProduct_stock(product.getProduct_stock());
        productDto.setProduct_like(product.getProduct_like());
        return productDto;
    }
}
