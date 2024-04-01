package com.DogFoot.adpotAnimal.order.service;

import com.DogFoot.adpotAnimal.order.entity.Delivery;
import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import com.DogFoot.adpotAnimal.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    // TODO : 유저, 상품 Repository 추가

    // 주문
    // Transactional -> 오류가 난다면 다시 롤백, 성공할 경우 리턴
    @Transactional
    public Long order (Long MemberId, Long Product, int count) {
        Member member = memberRepository.findOne(MemberId);
        Product product = productRepository.findOne(productId); // TODO : Member(User), Product Repository 메서드 추가

        // 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // TODO : 회원 엔티티 연결

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(product, product.getPrice(), count); // TODO : 상품 엔티티 연결

        // 주문 생성
        Order order = Order.createOrder (member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder (Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

}
