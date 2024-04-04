package com.DogFoot.adpotAnimal.cart.service;

import com.DogFoot.adpotAnimal.cart.dto.CartDto;
import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.cart.repository.CartRepository;
import com.DogFoot.adpotAnimal.product.dto.ProductDto;
import com.DogFoot.adpotAnimal.product.entity.ProductEntity;
import com.DogFoot.adpotAnimal.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    @Autowired
    public CartService(CartRepository cartRepository,ProductRepository productRepository){
        this.cartRepository=cartRepository;
        this.productRepository=productRepository;
    }

    public void addCart(CartDto cartDto) {
        CartEntity cartEntity = CartDto.toEntity(cartDto);
        cartRepository.save(cartEntity);
    }

    public List<CartDto> getCartItemsByUserId(String userId) {
        List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
        return cartEntities.stream()
                .map(cartEntity -> {
                    ProductEntity productEntity = productRepository.findById(cartEntity.getProductId()).orElse(null);
                    if (productEntity != null) {
                        return CartDto.fromEntity2(cartEntity, productEntity.getProduct_name(), productEntity.getPrice());
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
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
        return CartDto.fromEntity(updatedEntity);
    }
    public CartDto decreaseItemCount(Long itemId){
        Optional<CartEntity> opCartEntity = cartRepository.findById(itemId);
        CartEntity cartEntity = opCartEntity.get();
        if(cartEntity.getCnt()>1){
            cartEntity.setCnt(cartEntity.getCnt()-1);
            CartEntity updatedEntity = cartRepository.save(cartEntity);
            return CartDto.fromEntity(updatedEntity);
        }
        else{
            throw new IllegalArgumentException("감소 불가");
        }
    }
}