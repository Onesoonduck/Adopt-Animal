package com.DogFoot.adpotAnimal.cart.repository;

import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    public List<CartEntity> findByUserId(String userId);
}
