package com.DogFoot.adpotAnimal.cart.entity;

import com.DogFoot.adpotAnimal.product.entity.ProductEntity;
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

    @Column(name = "cnt")
    private int cnt;

    @Column(name="product_id")
    private Long productId;


}
