package com.DogFoot.adpotAnimal.products.dto;

import com.DogFoot.adpotAnimal.categories.entity.Category;
import com.DogFoot.adpotAnimal.products.controller.ProductController;
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
    private Long categoryId;

    public ProductDto(String productName, int productPrice, int productStock, String productImg, Long categoryId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productImg = productImg;
        this.categoryId = categoryId;
        this.productLike = 0;
    }

    public static Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setProductId(productDto.getId());
        product.setProductPrice(productDto.getProductPrice());
        product.setProductName(productDto.getProductName());
        product.setProductStock(productDto.getProductStock());
        product.setProductLike(productDto.getProductLike());
        product.setProductImg(productDto.getProductImg());
        product.setProductId(productDto.getCategoryId());
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
        productDto.setCategoryId(product.getCategory().getCategoryId());
        return productDto;
    }

}
