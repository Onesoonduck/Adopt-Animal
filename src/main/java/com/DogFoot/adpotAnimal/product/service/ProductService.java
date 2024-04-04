package com.DogFoot.adpotAnimal.product.service;

import com.DogFoot.adpotAnimal.product.entity.ProductEntity;
import com.DogFoot.adpotAnimal.product.dto.ProductDto;
import com.DogFoot.adpotAnimal.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity createProduct(ProductDto productDto) {
        ProductEntity product = new ProductEntity(
            productDto.getCategory_id(),
            productDto.getPrice(),
            productDto.getProduct_name(),
            productDto.getProduct_stock(),
            productDto.getLikes()
        );
        return productRepository.save(product);
    }

    public List<ProductEntity> findAll() {
        return productRepository.findAll();
    }


    @Transactional
    public void deleteProduct(Long id) {
        ProductEntity existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        productRepository.delete(existingProduct);
    }

    public ProductEntity findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    public ProductEntity updateProduct(Long id, ProductDto updatedProductDto) {
        ProductEntity existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        existingProduct.setCategory_id(updatedProductDto.getCategory_id());
        existingProduct.setPrice(updatedProductDto.getPrice());
        existingProduct.setProduct_name(updatedProductDto.getProduct_name());
        existingProduct.setProduct_stock(updatedProductDto.getProduct_stock());
        existingProduct.setLikes(updatedProductDto.getLikes());
        return productRepository.save(existingProduct);
    }
}
