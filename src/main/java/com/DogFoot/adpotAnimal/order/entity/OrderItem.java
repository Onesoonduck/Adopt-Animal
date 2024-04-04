package com.DogFoot.adpotAnimal.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;


    public void setOrder (Order order) {
        this.order = order;
    }

    public void setProduct (Product product) {
        this.product = product;
    }

    public void setOrderPrice (int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setCount (int count) {
        this.count = count;
    }

    // 추문 시 주문상품에 상품, 주문 당시 가격, 개수 추가 / 상품 재고량 감소
    public static OrderItem createOrderItem(Product product, int orderPrice, int count) { // TODO : 회원 도메인과 추후 연결
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

//        product.removeStock(count);
        // TODO : 상품 도메인과 추후 연결

        return orderItem;
    }


    // 제품 취소 시 재고 원상복구
    public void cancel() {
//        getProduct().addStock(count);
        // TODO : 상품 도메인과 추후 연결
    }

    // 가격 총액 = 제품 가격 * 개수
    public int getTotalPrice () {
        return this.orderPrice * this.count;
    }
}
