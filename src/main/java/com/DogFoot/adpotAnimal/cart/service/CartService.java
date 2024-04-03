package com.DogFoot.adpotAnimal.cart.service;

import com.DogFoot.adpotAnimal.cart.dto.CartDto;
import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    private CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository){
        this.cartRepository=cartRepository;
    }

    public void addCart(CartDto cartDto) {
        CartEntity cartEntity = CartDto.toEntity(cartDto);
        cartRepository.save(cartEntity);
    }
    public List<CartDto> getCartItemsByUserId(String userId) {
        List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
        return cartEntities.stream()
                .map(CartDto::fromDto)
                .collect(Collectors.toList());
    }
//    product merge후 바꿔야할 클래스    
//    public List<CartDto> getCartItemsByUserId(String userId) {
//        List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
//        return cartEntities.stream()
//                .map(cartEntity -> {
//                    CartDto cartDto = CartDto.fromDto(cartEntity);
//                    Long productId = cartEntity.getProductId();
//                    Optional<Product> productOptional = productRepository.findById(productId);
//                    productOptional.ifPresent(product -> {
//                        cartDto.setProductName(product.getName());
//                        cartDto.setProductPrice(product.getPrice());
//                    });
//                    return cartDto;
//                })
//                .collect(Collectors.toList());
//    }
    @Transactional
    public void deleteCartItems(List<Long> itemId){
        for(Long itemIds:itemId){
            cartRepository.deleteById(itemIds);
        }
    }
    public CartDto increaseItemCount(Long itemId){
        Optional<CartEntity> opCartEntity = cartRepository.findById(itemId);
        CartEntity cartEntity = opCartEntity.get();
        cartEntity.setCnt(cartEntity.getCnt()+1);
        CartEntity updatedEntity = cartRepository.save(cartEntity);
        return CartDto.fromDto(updatedEntity);
    }
    public CartDto decreaseItemCount(Long itemId){
        Optional<CartEntity> opCartEntity = cartRepository.findById(itemId);
        CartEntity cartEntity = opCartEntity.get();
        if(cartEntity.getCnt()>1){
            cartEntity.setCnt(cartEntity.getCnt()-1);
            CartEntity updatedEntity = cartRepository.save(cartEntity);
            return CartDto.fromDto(updatedEntity);
        }
        else{
            throw new IllegalArgumentException("감소 불가");
        }
    }
}