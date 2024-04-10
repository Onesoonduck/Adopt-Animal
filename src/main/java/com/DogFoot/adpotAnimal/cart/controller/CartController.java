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
    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/{user-id}")
    public ResponseEntity <List<CartDto>> getCartItemsByUserId(@PathVariable("user-id") String userId){
        List<CartDto> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @DeleteMapping
    public ResponseEntity<String> deleteCartItems(@RequestBody List<Long> itemIds){
        cartService.deleteCartItems(itemIds);
        return ResponseEntity.ok("선택한 물품이 삭제되었습니다.");
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @PatchMapping("/{cartId}/increase")
    public ResponseEntity<CartDto> increaseItemCount(@PathVariable Long cartId) {
        CartDto updatedCartDto = cartService.increaseItemCount(cartId);
        return ResponseEntity.ok(updatedCartDto);
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @PatchMapping("/{cartId}/decrease")
    public ResponseEntity<CartDto> decreaseItemCount(@PathVariable Long cartId) {
        CartDto updatedCartDto = cartService.decreaseItemCount(cartId);
        return ResponseEntity.ok(updatedCartDto);
    }
}
