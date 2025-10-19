package com.food_delivery.javaeats.service;

import com.food_delivery.javaeats.dto.reponse.ModifyCartItemResponse;
import com.food_delivery.javaeats.dto.request.ModifyCartItemRequest;

public interface CartService {
    ModifyCartItemResponse modifyCartItem(Long cartItemId, ModifyCartItemRequest request, Long userId);
}
