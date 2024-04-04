package com.DogFoot.adpotAnimal.product.repository;


import com.DogFoot.adpotAnimal.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {}
