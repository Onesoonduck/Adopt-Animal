package com.DogFoot.adpotAnimal.cart.controller;

import com.DogFoot.adpotAnimal.cart.dto.CartDto;
import com.DogFoot.adpotAnimal.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart/items")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService=cartService;
    }

    @PostMapping
    public ResponseEntity<CartDto> addCart(@RequestBody CartDto cartDto){
        cartService.addCart(cartDto);
        return ResponseEntity.ok(cartDto);
    }
    @GetMapping("/{user-id}")
    public ResponseEntity <List<CartDto>> getCartItemsByUserId(@PathVariable("user-id") String userId){
        List<CartDto> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteCartItems(@RequestBody List<Long> itemIds){
        cartService.deleteCartItems(itemIds);
        return ResponseEntity.ok("선택한 물품이 삭제되었습니다.");
    }
    @PatchMapping("/{itemId}/increase")
    public ResponseEntity<CartDto> increaseItemCount(@PathVariable Long itemId) {
        CartDto updatedCartDto = cartService.increaseItemCount(itemId);
        return ResponseEntity.ok(updatedCartDto);
    }
    @PatchMapping("/{itemId}/decrease")
    public ResponseEntity<CartDto> decreaseItemCount(@PathVariable Long itemId) {
        CartDto updatedCartDto = cartService.decreaseItemCount(itemId);
        return ResponseEntity.ok(updatedCartDto);
    }
}
