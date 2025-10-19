package com.food_delivery.javaeats.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCartItemResponse {
    private boolean success;
    private String message;
    private CartItemDTO cartItem;
    private CartDTO cart;
}
