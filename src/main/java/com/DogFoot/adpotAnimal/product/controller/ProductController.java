package com.DogFoot.adpotAnimal.product.controller;

import com.DogFoot.adpotAnimal.product.dto.ProductDto;
import com.DogFoot.adpotAnimal.product.entity.ProductEntity;
import com.DogFoot.adpotAnimal.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 새로운 상품 생성
    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductDto productDto) {
        ProductEntity createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // 모든 상품 조회
    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // 특정 ID의 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable Long id) {
        ProductEntity product = productService.findProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // 특정 ID의 상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @RequestBody ProductDto updatedProductDto) {
        ProductEntity updatedProduct = productService.updateProduct(id, updatedProductDto);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // 특정 ID의 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
