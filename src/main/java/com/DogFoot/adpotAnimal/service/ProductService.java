package com.DogFoot.adpotAnimal.service;

import com.DogFoot.adpotAnimal.entity.Product;
import com.DogFoot.adpotAnimal.dto.ProductDto;
import com.DogFoot.adpotAnimal.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.*;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product(
            productDto.getCategory_id(),
            productDto.getPrice(),
            productDto.getProduct_name(),
            productDto.getProduct_stock(),
            productDto.getLike()
        );
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }


    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        productRepository.delete(existingProduct);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    public Product updateProduct(Long id, ProductDto updatedProductDto) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        existingProduct.setCategory_id(updatedProductDto.getCategory_id());
        existingProduct.setPrice(updatedProductDto.getPrice());
        existingProduct.setProduct_name(updatedProductDto.getProduct_name());
        existingProduct.setProduct_stock(updatedProductDto.getProduct_stock());
        existingProduct.setLike(updatedProductDto.getLike());
        return productRepository.save(existingProduct);
    }
}
