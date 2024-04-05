package com.DogFoot.adpotAnimal.repository;


import com.DogFoot.adpotAnimal.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
