package com.DogFoot.adpotAnimal.cart.service;

import com.DogFoot.adpotAnimal.cart.dto.CartDto;
import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.cart.repository.CartRepository;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.repository.ProductRepository;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UsersRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository,ProductRepository productRepository,
                       UsersRepository userRepository){
        this.cartRepository=cartRepository;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }

    public void addCart(CartDto cartDto) {
        CartEntity cartEntity = CartDto.toEntity(cartDto);
        cartRepository.save(cartEntity);
    }

    public List<CartDto> getCartItemsByUserId(String userId) {
        List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
        return cartEntities.stream()
                .map(cartEntity -> {
                    Optional<Product> productOptional = productRepository.findById(cartEntity.getProductId());
                    Product productEntity = productOptional.orElseThrow(() -> new EntityNotFoundException("Product not found"));
                    if (productEntity != null) {

                        return CartDto.fromEntity2(cartEntity, productEntity.getProductName(), productEntity.getProductPrice());

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
        if (opCartEntity.isPresent()){
            CartEntity cartEntity = opCartEntity.get();
            cartEntity.setCnt(cartEntity.getCnt()+1);
            CartEntity updatedEntity = cartRepository.save(cartEntity);
            return CartDto.fromEntity(updatedEntity);
        }
        else{
            throw new EntityNotFoundException("카트 아이템을 찾을 수 없습니다.");
        }

    }
    public CartDto decreaseItemCount(Long itemId){
        Optional<CartEntity> opCartEntity = cartRepository.findById(itemId);
        if(opCartEntity.isPresent()){
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
        else{
            throw new EntityNotFoundException("카트 아이템을 찾을 수 없습니다.");
        }
    }
}