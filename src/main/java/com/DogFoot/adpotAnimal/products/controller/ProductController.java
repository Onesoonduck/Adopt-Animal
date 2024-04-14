package com.DogFoot.adpotAnimal.products.controller;

import com.DogFoot.adpotAnimal.order.service.OrderService;
import com.DogFoot.adpotAnimal.products.dto.ProductDto;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 새로운 상품 생성
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        Product createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // 모든 상품 조회
    @GetMapping("/lists")
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productService.findAll(pageable);
        return productPage.map(ProductDto::fromDto);
    }

    // 특정 ID의 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.findProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // 특정 ID의 상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDto updatedProductDto) {
        Product updatedProduct = productService.updateProduct(id, updatedProductDto);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // 특정 ID의 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 상품수 조회
    @GetMapping("/api/productCount")
    public ResponseEntity<Long> getUsersCount(HttpServletResponse response) {
        Long productCount = productService.getProductCount();
        return ResponseEntity.ok(productCount);
    }
}
