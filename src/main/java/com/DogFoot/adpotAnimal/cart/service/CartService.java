package com.DogFoot.adpotAnimal.cart.service;

import com.DogFoot.adpotAnimal.cart.dto.CartDto;
import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.cart.repository.CartRepository;
import com.DogFoot.adpotAnimal.products.entity.Product;
import com.DogFoot.adpotAnimal.products.repository.ProductRepository;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    public CartService(CartRepository cartRepository,ProductRepository productRepository){
        this.cartRepository=cartRepository;
        this.productRepository=productRepository;
    }

    public void addCart(CartDto cartDto) {
        CartEntity cartEntity = CartDto.toEntity(cartDto);
        cartRepository.save(cartEntity);
    }

    public Page<CartDto> getCartItemsByUserId(String userId, int page, int size) {
        // 페이지 번호와 사이즈가 음수인 경우를 처리
        if (page <= 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid page or size value");
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CartEntity> cartEntitiesPage = cartRepository.findByUserId(userId, pageable);

        // 카트 아이템을 페이지로 반환
        return cartEntitiesPage.map(cartEntity -> {
            Optional<Product> productOptional = productRepository.findById(cartEntity.getProductId());
            Product productEntity = productOptional.orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + cartEntity.getProductId()));
            return CartDto.fromEntity2(cartEntity, productEntity.getProductName(), productEntity.getProductPrice());
        });
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