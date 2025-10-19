package com.food_delivery.javaeats.exception.custom;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(Long menuItemId, Integer available) {
        super(String.format("Insufficient stock for menu item %d. Available quantity: %d",
                menuItemId, available));
    }

}
