package com.DogFoot.adpotAnimal.order.service;

import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    // TODO : 유저, 상품 Repository 추가

    // 주문 생성
    public Order create (Member member, Delivery delivery, List<OrderItem> orderItems) {
        Order createOrder = Order.createOrder(member, delivery, orderItems);

        return orderRepository.save(createOrder);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Found OrderId: " + id));
    }



    public List<Order> findAllByMemberId (Long memberId) {
        return orderRepository.findAllByMember_id(memberId);
    }

    public void deleteById (Long id) {
        findById(id);

        orderRepository.deleteById(id);
    }

    public void cancel (Long id) {
        findById(id).cancel();
    }

    public void delivery (Long id) {
        findById(id).delivery();
    }

    public void complete (Long id) {
        findById(id).complete;
    }

}
