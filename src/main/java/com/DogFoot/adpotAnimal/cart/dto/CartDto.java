package com.DogFoot.adpotAnimal.cart.dto;

import com.DogFoot.adpotAnimal.cart.entity.CartEntity;


import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long cartId;
    private String userId;
    private Long productId;
    private String productName;
    private int productPrice;
    private int cnt;

    public static CartEntity toEntity(CartDto cartDto) {
        return CartEntity.builder()
                .cartId(cartDto.getCartId())
                .userId(cartDto.getUserId())
                .productId(cartDto.getProductId())
                .cnt(cartDto.getCnt())
                .build();
    }

    public static CartDto fromEntity2(CartEntity cartEntity, String productName, int productPrice) {
        return CartDto.builder()
                .cartId(cartEntity.getCartId())
                .userId(cartEntity.getUserId())
                .productId(cartEntity.getProductId())
                .cnt(cartEntity.getCnt())
                .productName(productName)
                .productPrice(productPrice)
                .build();
    }

    public static CartDto fromEntity(CartEntity cartEntity) {
        return CartDto.builder()
                .cartId(cartEntity.getCartId())
                .userId(cartEntity.getUserId())
                .productId(cartEntity.getProductId())
                .cnt(cartEntity.getCnt())
                .build();
    }
}
