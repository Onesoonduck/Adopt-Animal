package com.DogFoot.adpotAnimal.order.entity;

import com.DogFoot.adpotAnimal.order.dto.OrderTableDto;
import com.DogFoot.adpotAnimal.order.dto.OrderResponse;
import com.DogFoot.adpotAnimal.products.dto.ProductDto;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // Orders 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonManagedReference // 순환참조를 막기 위한 어노테이션이지만 Dto를 쓰는 방법도?
    private Users users;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

//     양방향 매핑 (연관관계 메소드)
    public void setMember(Users users) {
        this.users = users;
        users.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    //주문 생성
    public static Order createOrder(Users users, Delivery delivery, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setUsers(users);

        if (delivery != null) {
            order.setDelivery(delivery);
        }

        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 주문 취소
    // 배송 중인 상품, 배송이 완료된 상품, 이미 취소가 된 상품은 취소 불가
    public void cancel() {
        if (this.orderStatus == OrderStatus.DELIVERY) {
            throw new IllegalStateException("배송 중인 상품은 취소가 불가능합니다.");
        }
        if (this.orderStatus == OrderStatus.COMPLETE) {
            throw new IllegalStateException("이미 배송이 완료된 상품은 취소가 불가능합니다.");
        }
        if (this.orderStatus == OrderStatus.CANCEL) {
            throw new IllegalStateException("이미 취소된 상품입니다.");
        }
        if (this.orderStatus == OrderStatus.REFUND) {
            throw new IllegalStateException("이미 환불된 상품입니다.");
        }

        this.setOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 주문 배송
    public void delivery() {
        if(this.orderStatus != OrderStatus.ORDER) {
            throw new IllegalStateException("이미 배송이 시작되었거나, 취소된 상품입니다.");
        }
        this.setOrderStatus(OrderStatus.DELIVERY);
    }

    // 주문 배송 완료
    public void complete() {
        if (this.orderStatus == OrderStatus.CANCEL) {
            throw new IllegalStateException("이미 배송 완료 처리된 상품입니다.");
        }
        this.setOrderStatus(OrderStatus.COMPLETE);
    }

    // 배송 물품 환불
    public void refund() {
        if (this.orderStatus != OrderStatus.COMPLETE) {
            throw new IllegalStateException("환불 처리 불가능한 상태의 상품입니다. (환불 가능한 상품 : 배송이 완료된 상품)");
        }
        this.setOrderStatus(OrderStatus.REFUND);
    }

    // 주문의 전체 가격
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
    public OrderTableDto toTableDto() {
        OrderTableDto orderTableDto = OrderTableDto.builder()
            .id(id)
            .orderDate(orderDate)
            .firstOrderItem(orderItems.get(0).getProduct().getProductName())
            .orderCount(orderItems.size())
            .totalPrice((long) getTotalPrice())
            .orderUserId(getUsers().getUserId())
            .orderStatus(getOrderStatus())
            .build();
        return orderTableDto;
    }
}
