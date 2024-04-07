package com.DogFoot.adpotAnimal.order.repository;

import com.DogFoot.adpotAnimal.order.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
