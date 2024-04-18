package com.DogFoot.adpotAnimal.order.repository;

import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
