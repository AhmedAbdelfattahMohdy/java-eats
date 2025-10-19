package com.food_delivery.javaeats.exception.custom;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String message) {
        super(message);
    }

    public CartItemNotFoundException(Long cartItemId) {
        super("Cart item with id " + cartItemId + " not found");
    }

}
