package com.DogFoot.adpotAnimal.cart.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "product_id")
    private Long productId;

    private int cnt;

      //product merge후 추가
//    @ManyToOne(fetch = FetchType.LAZY) 
//    @JoinColumn(name = "product_id") 
//    private Product product; 
//    
//    @Transient 
//    private String productName;
//
//    @Transient
//    private double productPrice;
//    public void setProductInfo() {
//        if (product != null) {
//            this.productName = product.getName();
//            this.productPrice = product.getPrice();
//        }
//    }
    
}
