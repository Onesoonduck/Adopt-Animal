package com.DogFoot.adpotAnimal.cart.service;

import com.DogFoot.adpotAnimal.cart.dto.CartDto;
import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository){
        this.cartRepository=cartRepository;
    }

    public void addCart(CartDto cartDto) {
        CartEntity cartEntity=new CartEntity();
        cartEntity.setUserId(cartDto.getUserId());
        cartEntity.setProductId(cartDto.getProductId());
        cartEntity.setCnt(cartDto.getCnt());
        cartRepository.save(cartEntity);
    }
}