package com.DogFoot.adpotAnimal.cart.service;

import com.DogFoot.adpotAnimal.cart.dto.CartDto;
import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.cart.repository.CartRepository;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.repository.ProductRepository;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Users user = userRepository.findByUserId(userId)
                .orElse(null);

        if (user != null) { // 사용자를 찾았는지 확인합니다.
            List<CartEntity> cartEntities = cartRepository.findByUsers(user); // 사용자와 연관된 카트 엔티티를 찾습니다.
            return cartEntities.stream()
                    .map(cartEntity -> CartDto.fromEntity(cartEntity)) // 찾은 카트 엔티티를 CartDto로 변환합니다.
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // 사용자가 없으면 빈 리스트를 반환합니다.
        }
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