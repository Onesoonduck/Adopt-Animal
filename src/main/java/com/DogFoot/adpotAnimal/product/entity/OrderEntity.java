package com.DogFoot.adpotAnimal.product.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColum(name = "user_id)
//    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Column(nullable = false, name = "status")
    private String status;

    @Column(nullable = false, name = "product_cnt")
    private int productCnt;

    @Column(nullable = false, name = "total_price")
    private int totalPrice;

    @Column(nullable = false, name = "address")
    private String address;

    @Builder
    public OrderEntity(String status, int productCnt, int totalPrice, String address) {
        this.status = status;
        this.productCnt = productCnt;
        this.totalPrice = totalPrice;
        this.address = address;
    }

}