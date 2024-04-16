package com.DogFoot.adpotAnimal.cart.controller;

import com.DogFoot.adpotAnimal.cart.dto.CartDto;
import com.DogFoot.adpotAnimal.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        cartDto.setUserId(currentUserId);
        cartService.addCart(cartDto);
        return ResponseEntity.ok(cartDto);
    }
//    @GetMapping
//    public ResponseEntity<List<CartDto>> getCartItemsByCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUserId = authentication.getName();
//
//        List<CartDto> cartItems = cartService.getCartItemsByUserId(currentUserId);
//        return ResponseEntity.ok(cartItems);
//    }

    //테스트용
    @GetMapping("/{user-id}")
    public ResponseEntity<Page<CartDto>> getCartItemsByUserId(@PathVariable("user-id") String userId,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<CartDto> cartItems = cartService.getCartItemsByUserId(userId, page, size);
        return ResponseEntity.ok(cartItems);
    }


    @DeleteMapping
    public ResponseEntity<String> deleteCartItems(@RequestBody List<Long> itemIds){
        cartService.deleteCartItems(itemIds);
        return ResponseEntity.ok("선택한 물품이 삭제되었습니다.");
    }

    @PatchMapping("/{cartId}/increase")
    public ResponseEntity<CartDto> increaseItemCount(@PathVariable Long cartId) {
        CartDto updatedCartDto = cartService.increaseItemCount(cartId);
        return ResponseEntity.ok(updatedCartDto);
    }

    @PatchMapping("/{cartId}/decrease")
    public ResponseEntity<CartDto> decreaseItemCount(@PathVariable Long cartId) {
        CartDto updatedCartDto = cartService.decreaseItemCount(cartId);
        return ResponseEntity.ok(updatedCartDto);
    }
}
