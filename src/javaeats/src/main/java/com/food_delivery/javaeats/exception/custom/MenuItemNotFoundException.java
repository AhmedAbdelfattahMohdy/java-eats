package com.food_delivery.javaeats.exception.custom;

public class MenuItemNotFoundException extends RuntimeException{
    public MenuItemNotFoundException(String message) {
        super(message);
    }
    public MenuItemNotFoundException(Long menuItemId) {
        super("Menu item with id " + menuItemId + " not found");
    }
}
