package com.food_delivery.javaeats.exception.custom;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }

    public CartNotFoundException(Long cartId) {
        super("Cart with id " + cartId + " not found");
    }
}
