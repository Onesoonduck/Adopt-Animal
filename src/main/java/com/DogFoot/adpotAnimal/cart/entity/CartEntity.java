package com.DogFoot.adpotAnimal.cart.entity;


import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartId;

    @Column(name = "cnt")
    private int cnt;

    @Column(name="product_id")
    private Long productId;

    // CartEntity 연관 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<Product> products;
}
