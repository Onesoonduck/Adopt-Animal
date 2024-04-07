package com.DogFoot.adpotAnimal.cart.dto;

import com.DogFoot.adpotAnimal.cart.entity.CartEntity;


import lombok.*;

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
        CartEntity cartEntity = new CartEntity();
        cartEntity.setCartId(cartDto.getCartId());
        cartEntity.setCnt(cartDto.getCnt());
        cartEntity.setProductId(cartDto.getProductId());
        return cartEntity;
    }

    public static CartDto fromEntity(CartEntity cartEntity) {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cartEntity.getCartId());
        cartDto.setUserId(cartEntity.getUsers().getUserId());
        cartDto.setProductId(cartEntity.getProductId());
        cartDto.setCnt(cartEntity.getCnt());
        cartDto.setProductName(cartEntity.getProducts().get(0).getProduct_name());
        cartDto.setProductPrice(cartEntity.getProducts().get(0).getProduct_price());
        return cartDto;
    }

}
