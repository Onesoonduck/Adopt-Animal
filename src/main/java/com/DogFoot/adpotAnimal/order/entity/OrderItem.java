package com.DogFoot.adpotAnimal.order.entity;

import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.products.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    private int price;

    private int count;

    // OrderItem 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cart_id")
//    private CartEntity cart;


    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // 추문 시 주문상품에 상품, 주문 당시 가격, 개수 추가 / 상품 재고량 감소
    public static OrderItem createOrderItem(Product product, int price, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setPrice(price);
        orderItem.setCount(count);

        product.removeStock(count);

        return orderItem;
    }

    // 제품 취소 시 재고 원상복구
    public void cancel() {
        getProduct().addStock(count);
    }

    // 제품 환불 시 재고 원상 복구
    public void refund() {
        getProduct().addStock(count);
    }

    // 가격 총액 = 제품 가격 * 개수
    public int getTotalPrice() {
        return this.price * this.count;
    }
}
