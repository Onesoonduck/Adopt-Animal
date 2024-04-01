package com.DogFoot.adpotAnimal.order.repository;

import com.DogFoot.adpotAnimal.order.entity.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save (Order order) {
        em.persist(order);
    }

    public Order findOne (Long id) {
        return em.find(Order.class, id);
    }
}
