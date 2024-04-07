package com.DogFoot.adpotAnimal.products.service;

import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.dto.ProductDto;
import com.DogFoot.adpotAnimal.products.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProduct_price(productDto.getProduct_price());
        product.setProduct_name(productDto.getProduct_name());
        product.setProduct_stock(productDto.getProduct_stock());
        product.setProduct_like(productDto.getProduct_like());

        // 카테고리 정보가 있다면 설정
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
        existingProduct.setProduct_price(updatedProductDto.getProduct_price());
        existingProduct.setProduct_name(updatedProductDto.getProduct_name());
        existingProduct.setProduct_stock(updatedProductDto.getProduct_stock());

        existingProduct.setProduct_like(updatedProductDto.getProduct_like());

        return productRepository.save(existingProduct);
    }

}
