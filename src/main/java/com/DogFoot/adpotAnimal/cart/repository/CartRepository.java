package com.DogFoot.adpotAnimal.cart.repository;

import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.users.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    public Page<CartEntity> findByUserId(String userId, Pageable pageable);
}
