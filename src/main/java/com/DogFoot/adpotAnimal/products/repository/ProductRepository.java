package com.DogFoot.adpotAnimal.products.repository;


import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.products.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    long countByCategoryCategoryId(Long categoryId);
}
