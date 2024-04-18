package com.DogFoot.adpotAnimal.products.dto;

import com.DogFoot.adpotAnimal.products.entity.Product;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private int productPrice;
    private String productName;
    private int productStock;
    private int productLike;
    private String productImg;

    public static Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setProductId(productDto.getId());
        product.setProductPrice(productDto.getProductPrice());
        product.setProductName(productDto.getProductName());
        product.setProductStock(productDto.getProductStock());
        product.setProductLike(productDto.getProductLike());
        product.setProductImg(productDto.getProductImg());
        return product;
    }

    public static ProductDto fromDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getProductId());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductName(product.getProductName());
        productDto.setProductStock(product.getProductStock());
        productDto.setProductLike(product.getProductLike());
        productDto.setProductImg(product.getProductImg());
        return productDto;
    }
}
