package com.DogFoot.adpotAnimal.order.service;

import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.repository.OrderRepository;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UsersRepository usersRepository;

    // 주문 생성
    public Order create (Users users, Delivery delivery, List<OrderItem> orderItems) {
        Order createOrder = Order.createOrder(users, delivery, orderItems);

        return orderRepository.save(createOrder);
    }

    // 주문 조회
    public Order findById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Found OrderId: " + id));
    }

    // 회윈의 주문 조회
    public List<Order> findAllByUsersId (Long usersId) {
        return orderRepository.findAllByUsers_id(usersId);
    }

    // 주문 상태 삭제
    public void deleteById (Long id) {
        findById(id);

        orderRepository.deleteById(id);
    }

    // 주문 상태 취소
    public void cancel (Long id) {
        findById(id).cancel();
    }

    // 주문 상태 배송
    public void delivery (Long id) {
        findById(id).delivery();
    }

    // 주문 상태 배송 완료
    public void complete (Long id) {
        findById(id).complete();
    }

}
