package com.food_delivery.javaeats.controller;

import com.food_delivery.javaeats.dto.reponse.ModifyCartItemResponse;
import com.food_delivery.javaeats.dto.request.ModifyCartItemRequest;
import com.food_delivery.javaeats.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart/items")
public class CartController {

    private final CartService cartService;

    // NOTE: for testing purpose only
    @GetMapping
    public String welcome() {
        return "Welcome to Cart API";
    }

    public ResponseEntity<ModifyCartItemResponse> modifyCartItem(
            @PathVariable("cartItemId") Long cartItemId,
            @RequestBody ModifyCartItemRequest request,
            @RequestHeader(value = "userId") Long userId) {
        log.info("User {} modifying cart item {} with new quantity {}", userId, cartItemId, request.getNewQuantity());
        ModifyCartItemResponse response = cartService.modifyCartItem(cartItemId, request, userId);
        log.info("ModifyCart operation completed for user {} - cart total: {}", userId, response.getCart().getTotalAmount());
        return ResponseEntity.ok(response);

    }

}
