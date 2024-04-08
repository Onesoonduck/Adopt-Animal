package com.DogFoot.adpotAnimal.products.repository;


import com.DogFoot.adpotAnimal.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
