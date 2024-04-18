package com.DogFoot.adpotAnimal.products.service;

import com.DogFoot.adpotAnimal.categories.service.CategoryService;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.dto.ProductDto;
import com.DogFoot.adpotAnimal.products.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.ParserRuleContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Product createProduct(ProductDto productDto) {
        Product product = Product.builder()
            .productName(productDto.getProductName())
            .productPrice(productDto.getProductPrice())
            .productStock(productDto.getProductStock())
            .productLike(productDto.getProductLike())
            .productImg(productDto.getProductImg())
            .category(categoryService.findByCategoryId(productDto.getCategoryId()))
            .build();
        return productRepository.save(product);
    }

    // 상품 리스트
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // 상품 리스트
    public Page<Product> findProductByCategory(Pageable pageable, String categoryId) {
        return productRepository.findByCategoryCategoryId(Long.parseLong(categoryId), pageable);
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
        existingProduct.setProductPrice(updatedProductDto.getProductPrice());
        existingProduct.setProductName(updatedProductDto.getProductName());
        existingProduct.setProductStock(updatedProductDto.getProductStock());
        existingProduct.setProductLike(updatedProductDto.getProductLike());

        return productRepository.save(existingProduct);
    }

    // 제품 수 조회
    public long getProductCount() {
        return productRepository.count();
    }

    // 카테고리 별 제품 수 조회
    public long getProductCountByCategory(Long categoryId) {
        return productRepository.countByCategoryCategoryId(categoryId);
    }
}
