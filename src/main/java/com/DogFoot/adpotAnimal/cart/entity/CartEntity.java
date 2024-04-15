package com.DogFoot.adpotAnimal.cart.entity;


import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "cnt")
    private int cnt;

    @Column(name="product_id")
    private Long productId;
}
