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
    private int cnt;

    public static CartEntity toEntity(CartDto cartDto) {
        CartEntity cartEntity = new CartEntity();
        cartEntity.setCartId(cartDto.getCartId());
        cartEntity.setUserId(cartDto.getUserId());
        cartEntity.setProductId(cartDto.getProductId());
        cartEntity.setCnt(cartDto.getCnt());
        return cartEntity;
    }

    public static CartDto fromDto(CartEntity cartEntity) {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cartEntity.getCartId());
        cartDto.setUserId(cartEntity.getUserId());
        cartDto.setProductId(cartEntity.getProductId());
        cartDto.setCnt(cartEntity.getCnt());
        return cartDto;
    }
}
