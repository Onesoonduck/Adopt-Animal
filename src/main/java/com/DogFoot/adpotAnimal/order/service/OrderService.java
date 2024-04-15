package com.DogFoot.adpotAnimal.order.service;

import com.DogFoot.adpotAnimal.order.dto.OrderTableDto;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.repository.OrderRepository;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UsersRepository usersRepository;

    // 주문 생성
    public Order create(Users users, Delivery delivery, List<OrderItem> orderItems) {
        Order createOrder = Order.createOrder(users, delivery, orderItems);

        return orderRepository.save(createOrder);
    }

    // 각 회원 주문 조회
    public Order findById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Not Found OrderId: " + id));
    }

    // 모든 회윈의 주문 조회
    public List<Order> findAllByUsersId(Long usersId) {
        return orderRepository.findAllByUsers_id(usersId);
    }

    // 주문 상태 삭제
    @Transactional
    public void deleteById(Long id) {
        Order order = findById(id);

        // 예외사항
        if (order == null) {
            throw new IllegalStateException("삭제할 주문이 존재하지 않습니다. Id: " + id);
        }

        orderRepository.deleteById(id);
    }

    // 주문 상태 취소
    @Transactional
    public void cancel(Long id) {
        Order order = findById(id);

        // 예외사항
        if (order == null) {
            throw new IllegalStateException("취소할 주문이 존재하지 않습니다. Id: " + id);
        }

        order.cancel();
    }

    // 주문 상태 배송
    @Transactional
    public void delivery(Long id) {
        Order order = findById(id);

        // 예외사항
        if (order == null) {
            throw new IllegalStateException("배송 처리할 주문이 존재하지 않습니다. Id: " + id);
        }

        order.delivery();
    }

    // 주문 상태 배송 완료
    @Transactional
    public void complete(Long id) {
        Order order = findById(id);

        // 예외사항
        if (order == null) {
            throw new IllegalStateException("주문이 존재하지 않습니다. Id: " + id);
        }

        order.complete();
    }

    // 주문 상태 배송 이후 환불
    @Transactional
    public void refund(Long id) {
        Order order = findById(id);

        // 예외사항
        if (order == null) {
            throw new IllegalStateException("주문이 존재하지 않습니다. Id: " + id);
        }

        order.refund();
    }

    // 주문 수 조회
    public long getOrderCount() {
        return orderRepository.count();
    }

    // 관리자페이지 주문 목록 조회
    public Page<OrderTableDto> getOrderTable(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(Order::toTableDto);
    }
}
